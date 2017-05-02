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

可以清晰看出
由source转换成QueryPhase 再交由lucene的Searcher处理

debug的方式抓出来的堆栈 
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


parseSource 的 trace
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


一番分析之后  脉络应该很清楚了：
org.elasticsearch.search.query.QueryPhase.execute(SearchContext, IndexSearcher)
自己本身承担了最直接的searcher调用

同时QueryPhase还包括三个可选Phase ： AggregationPhase  SuggestPhase  RescorePhase
这个几个phase都是SearchPhase接口的实现  当然这个SearchPhase接口还有些其他实现
这几个执行先后顺序，代码和清晰

        aggregationPhase.preProcess(searchContext);
        boolean rescore = execute(searchContext, searchContext.searcher());// 这个是QueryPhase的execute
        if (rescore) { // only if we do a regular search
            rescorePhase.execute(searchContext);
        }
        suggestPhase.execute(searchContext);
        aggregationPhase.execute(searchContext);

--

下面就要分析 这些phase的execute细节
以及SearchService:parseSource 干了些啥
