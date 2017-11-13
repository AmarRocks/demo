# ElasticSearch 查询分析1 

## 调用堆栈  
```java
2608253	88	0	26515	org/elasticsearch/common/util/concurrent/AbstractRunnable:run:43
2608254	88	1	43747	org/elasticsearch/transport/TransportService$4:doRun:377
2608255	88	2	28339	org/elasticsearch/transport/RequestHandlerRegistry:processMessageReceived:85
2608272	88	3	25757	org/elasticsearch/transport/TransportRequestHandler:messageReceived:34
2608273	88	4	30831	org/elasticsearch/search/action/SearchServiceTransportAction$SearchQueryTransportHandler:messageReceived:365
2608274	88	5	30830	org/elasticsearch/search/action/SearchServiceTransportAction$SearchQueryTransportHandler:messageReceived:370
2608276	88	6	16551	org/elasticsearch/search/SearchService:executeQueryPhase:405
...
2611204	88	10	16550	org/elasticsearch/search/SearchService:loadOrExecuteQueryPhase:374
2611210	88	11	21305	org/elasticsearch/search/query/QueryPhase:execute:125
2611214	88	12	21307	org/elasticsearch/search/query/QueryPhase:execute:409
2611255	88	13	30468	org/apache/lucene/search/IndexSearcher:search:536
```

可以清晰看出  
由source转换成QueryPhase 再交由lucene的Searcher处理  

debug的方式抓出来的堆栈   
```java
ContextIndexSearcher(IndexSearcher).search(Query, Collector) line: 535	
QueryPhase.execute(SearchContext, IndexSearcher) line: 384	ntext, IndexSearcher) line: 384	
QueryPhase.execute(SearchContext) line: 113	
SearchService.loadOrExecuteQueryPhase(ShardSearchRequest, SearchContext, QueryPhase) line: 372	
SearchService.executeQueryPhase(ShardSearchRequest) line: 385	
SearchServiceTransportAction$SearchQueryTransportHandler.messageReceived(ShardSearchTransportRequest, TransportChannel) line: 368	
SearchServiceTransportAction$SearchQueryTransportHandler.messageReceived(TransportRequest, TransportChannel) line: 365	
SearchServiceTransportAction$SearchQueryTransportHandler(TransportRequestHandler<T>).messageReceived(T, TransportChannel, Task) line: 33	
RequestHandlerRegistry<Request>.processMessageReceived(Request, TransportChannel) line: 77	
TransportService$4.doRun() line: 376	
TransportService$4(AbstractRunnable).run() line: 37	
EsThreadPoolExecutor(ThreadPoolExecutor).runWorker(ThreadPoolExecutor$Worker) line: 1145	
ThreadPoolExecutor$Worker.run() line: 615	
Thread.run() line: 745	
```

parseSource 的 trace  
```java
2608253	88	0	26515	org/elasticsearch/common/util/concurrent/AbstractRunnable:run:43
2608254	88	1	43747	org/elasticsearch/transport/TransportService$4:doRun:377
2608255	88	2	28339	org/elasticsearch/transport/RequestHandlerRegistry:processMessageReceived:85
2608272	88	3	25757	org/elasticsearch/transport/TransportRequestHandler:messageReceived:34
2608273	88	4	30831	org/elasticsearch/search/action/SearchServiceTransportAction$SearchQueryTransportHandler:messageReceived:365
2608274	88	5	30830	org/elasticsearch/search/action/SearchServiceTransportAction$SearchQueryTransportHandler:messageReceived:370
2608276	88	6	16551	org/elasticsearch/search/SearchService:executeQueryPhase:405
2608277	88	7	16560	org/elasticsearch/search/SearchService:createAndPutContext:645
2608278	88	8	16561	org/elasticsearch/search/SearchService:createContext:703
2608419	88	9	16570	org/elasticsearch/search/SearchService:parseSource:879
```

一番分析之后  脉络应该很清楚了：  
`org.elasticsearch.search.query.QueryPhase.execute(SearchContext, IndexSearcher)`
自己本身承担了最直接的searcher调用  

同时QueryPhase还包括三个可选Phase ： `AggregationPhase`  `SuggestPhase`  `RescorePhase`   
这个几个phase都是SearchPhase接口的实现  当然这个SearchPhase接口还有些其他实现  
这几个执行先后顺序，代码和清晰  
```java
        aggregationPhase.preProcess(searchContext);
        boolean rescore = execute(searchContext, searchContext.searcher());// 这个是QueryPhase的execute
        if (rescore) { // only if we do a regular search
            rescorePhase.execute(searchContext);
        }
        suggestPhase.execute(searchContext);
        aggregationPhase.execute(searchContext);
```

下面就要分析 这些phase的execute细节  
以及SearchService:parseSource 干了些啥  

## lucene的IndexSearcher分析  
`InternalSearchHit`是elasticsearch内部结果的重要表示，具体可以参见org.elasticsearch.search.controller.SearchPhaseController.merge(ScoreDoc[], AtomicArray<? extends QuerySearchResultProvider>, AtomicArray<? extends FetchSearchResultProvider>, HasContextAndHeaders) 370行  
searchHit.getSourceAsString() 的结果就是这条记录的json表示   


下面的这个栈  说明了searchHit中的source的值是怎么设置上去的，，中间省略了一些  
```java
org.elasticsearch.index.fieldvisitor.FieldsVisitor.binaryField(org.apache.lucene.index.FieldInfo, byte[]) line: 139	
org.apache.lucene.codecs.compressing.CompressingStoredFieldsReader.readField(org.apache.lucene.store.DataInput, org.apache.lucene.index.StoredFieldVisitor, org.apache.lucene.index.FieldInfo, int) line: 216	
org.apache.lucene.codecs.compressing.CompressingStoredFieldsReader.visitDocument(int, org.apache.lucene.index.StoredFieldVisitor) line: 595	
org.apache.lucene.index.SegmentReader(org.apache.lucene.index.CodecReader).document(int, org.apache.lucene.index.StoredFieldVisitor) line: 81	
org.elasticsearch.common.lucene.index.ElasticsearchLeafReader(org.apache.lucene.index.FilterLeafReader).document(int, org.apache.lucene.index.StoredFieldVisitor) line: 405	
org.elasticsearch.search.fetch.FetchPhase.loadStoredFields(org.elasticsearch.search.internal.SearchContext, org.apache.lucene.index.LeafReaderContext, org.elasticsearch.index.fieldvisitor.FieldsVisitor, int) line: 413	
org.elasticsearch.search.fetch.FetchPhase.createSearchHit(org.elasticsearch.search.internal.SearchContext, org.elasticsearch.index.fieldvisitor.FieldsVisitor, int, int, java.util.List<java.lang.String>, org.apache.lucene.index.LeafReaderContext) line: 213	
org.elasticsearch.search.fetch.FetchPhase.execute(org.elasticsearch.search.internal.SearchContext) line: 178	
org.elasticsearch.search.SearchService.executeFetchPhase(org.elasticsearch.search.fetch.ShardFetchRequest) line: 605	
org.elasticsearch.search.action.SearchServiceTransportAction$FetchByIdTransportHandler<Request>.messageReceived(Request, org.elasticsearch.transport.TransportChannel) line: 408	
org.elasticsearch.search.action.SearchServiceTransportAction$FetchByIdTransportHandler<Request>.messageReceived(org.elasticsearch.transport.TransportRequest, org.elasticsearch.transport.TransportChannel) line: 405	
org.elasticsearch.search.action.SearchServiceTransportAction$FetchByIdTransportHandler<Request>(org.elasticsearch.transport.TransportRequestHandler<T>).messageReceived(T, org.elasticsearch.transport.TransportChannel, org.elasticsearch.tasks.Task) line: 33	
org.elasticsearch.transport.RequestHandlerRegistry<Request>.processMessageReceived(Request, org.elasticsearch.transport.TransportChannel) line: 77	
org.elasticsearch.transport.TransportService$4.doRun() line: 376	
org.elasticsearch.transport.TransportService$4(org.elasticsearch.common.util.concurrent.AbstractRunnable).run() line: 37	
org.elasticsearch.common.util.concurrent.EsThreadPoolExecutor(java.util.concurrent.ThreadPoolExecutor).runWorker(java.util.concurrent.ThreadPoolExecutor$Worker) line: 1142	
java.util.concurrent.ThreadPoolExecutor$Worker.run() line: 617	
java.lang.Thread.run() line: 745	

```

org.apache.lucene.codecs.compressing.CompressingStoredFieldsReader.document(int) 根据文档id 读取文档数据的地方  


