# KafkaElasticSearchPOC

useful links :- 
https://github.com/hakdogan/ElasticSearch
https://github.com/elastic/examples
https://mvnrepository.com/artifact/org.apache.kafka/kafka_2.11/1.0.0
https://www.elastic.co/downloads/kibana
https://www.elastic.co/guide/en/kibana/current/getting-started.html
https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started.html
=============================================================================

sample dataset :- 

each document has the following schema : 
{
    "account_number": 0,
    "balance": 16623,
    "firstname": "Bradshaw",
    "lastname": "Mckenzie",
    "age": 29,
    "gender": "F",
    "address": "244 Columbus Place",
    "employer": "Euron",
    "email": "bradshawmckenzie@euron.com",
    "city": "Hobucken",
    "state": "CO"
}

>> data was generated using :- 
www.json-generator.com/

>> url for sample data :- 
https://raw.githubusercontent.com/elastic/elasticsearch/master/docs/src/test/resources/accounts.json

========================================================================================================================

Setup kafka and elasticsearch :- 

running elastic search (https://www.elastic.co/guide/en/elasticsearch/reference/current/_installation.html) :
>>unzip folder 
>> go inside bin and run elasticsearch.bat
>> will start at default port :9200

running kibana :
https://www.elastic.co/guide/en/kibana/current/getting-started.html
https://www.elastic.co/guide/en/kibana/current/windows.html

>> unzip folder
>> go inside bin and run kibana.bat
>>configs at : $KIBANA_HOME/config/kibana.yml
>>http://localhost:5601 , open dev-tools for running queries .


Running zookeeper and kafka (https://dzone.com/articles/running-apache-kafka-on-windows-os)
>> download zookeeper and keep it in a directory 
>> go inside conf change zoo_sample.cfg to zoo.cfg 
>> go inside zoo.config , change location of dataDir to data folder inside zookeeper(also create this data folder)
>>add entry of ZOOKEEPER_HOME inside environment variables ,ZOOKEEPER_HOME = C:\zookeeper-3.4.7
>> add entry in path - ;%ZOOKEEPER_HOME%\bin;
>> default port is 2181 , can be changed in config file 
>> run zookeeper by running zkserver.bat from cmd or directly

>> unzip kafka 
>>inside config folder , edit server.properties , change location of logs.dir to kafka-logs folder inside kafka folder(also create the same)
>> if your zookeeper is running on some other port , you can edit “zookeeper.connect:2181” to your custom IP and port 
>> kafka port and broker id can also be changed in this only 
>> kafka will run on default port 9092
>>go to kafka/bin/windows /kafka-server-start.bat , as below 
.\bin\windows\kafka-server-start.bat .\config\server.properties
>> some useful commands
create topic : kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
start console producer : kafka-console-producer.bat --broker-list localhost:9092 --topic test
start console consumer : kafka-console-consumer.bat --zookeeper localhost:2181 --topic test
List Topics: kafka-topics.bat --list --zookeeper localhost:2181
Describe Topic: kafka-topics.bat --describe --zookeeper localhost:2181 --topic [Topic Name]
Read messages from beginning: kafka-console-consumer.bat --zookeeper localhost:2181 --topic [Topic Name] --from-beginning
Delete Topic: kafka-run-class.bat kafka.admin.TopicCommand --delete --topic [topic_to_delete] --zookeeper localhost:2181

=========================================================================================================
kibana :- 
>> open console in kibana 
>> GET _search
{
  "query": {
    "match_all": {}
  }
}


>> to check the cluster health 
curl -XGET 'localhost:9200/_cat/health?v&pretty'

>> list of nodes in a cluster :- 
GET /_cat/nodes?v

>> lets take a peek at are indexes :- 
GET /_cat/indices?v

>> create an index 
>>let’s create an index named "customer" and then list all the indexes again
>> 
PUT /customer?pretty
GET /_cat/indices?v



>> put something inside a customer index :- 
PUT /customer/_doc/1?pretty
{
  "name": "John Doe"
}

>> retrieve the document that we have just indexed :- 
GET /customer/_doc/1?pretty

>> delete an index 
DELETE /customer?pretty
GET /_cat/indices?v

>> syntax of commands : 
PUT /customer
PUT /customer/_doc/1
{
  "name": "John Doe"
}
GET /customer/_doc/1
DELETE /customer

<REST Verb> /<Index>/<Type>/<ID>

>> modifying your data :- 
 By default, you can expect a one second delay (refresh interval) from the time you index/update/delete your data until the time that it appears in your search results.
 This is an important distinction from other platforms like SQL wherein data is immediately available after a transaction is completed.
 
 PUT /customer/_doc/1?pretty
{
  "name": "John Doe"
}

if you call put for id again , document will be replaced 

PUT /customer/_doc/1?pretty
{
  "name": "Jane Doe"
}

put for new id will create a new document 
PUT /customer/_doc/2?pretty
{
  "name": "Jane Doe"
}

>> index a document without explicit id :
POST /customer/_doc?pretty
{
  "name": "Jane Doe"
}

"{\"name\": \"Jane Doe\"}"
>> updating a document in elastic search :- 
 Elasticsearch does not actually do in-place updates under the hood. Whenever we do an update, Elasticsearch deletes the old document and then indexes a new document with the update applied to it in one shot.
 POST /customer/_doc/1/_update?pretty
{
  "doc": { "name": "Jane Doe" }
}

>> script to update the document :- 
POST /customer/_doc/1/_update?pretty
{
  "script" : "ctx._source.age += 5"
}


>> deleting a document 
DELETE /customer/_doc/2?pretty
much more efficient to delete whole index rather then deleting them with delete api above

>> batch processing : 
 the following call indexes two documents (ID 1 - John Doe and ID 2 - Jane Doe) in one bulk operation:
POST /customer/_doc/_bulk?pretty
{"index":{"_id":"1"}}
{"name": "John Doe" }
{"index":{"_id":"2"}}
{"name": "Jane Doe" }



>>This example updates the first document (ID of 1) and then deletes the second document (ID of 2) in one bulk operation:
POST /customer/_doc/_bulk?pretty
{"update":{"_id":"1"}}
{"doc": { "name": "John Doe becomes Jane Doe" } }
{"delete":{"_id":"2"}}

=================================================================================================================================

lets run some simple searches :- 

two basic ways to run searches :- 
1. send search parameters through REST request URI 
2. send search parameters through REST request Body

>>  REST API for search is accessible from the _search endpoint. 
GET /bank/_search?q=*&sort=account_number:asc&pretty

>> same exact search using request body :-
GET /bank/_search
{
  "query": { "match_all": {} },
  "sort": [
    { "account_number": "asc" }
  ]
}

>>  Elasticsearch is completely done with the request and does not maintain any kind of server-side resources or open cursors into your results. This is in stark contrast
 to many other platforms such as SQL wherein you may initially get a partial subset of your query results up-front and then you 
have to continuously go back to the server if you want to fetch (or page through) the rest of the results using some kind of stateful server-side cursor.

===============================================================================================================================
data.hits
Query Language :- 
GET /bank/_search
{
  "query": { "match_all": {} }
}

>>GET /bank/_search
{
  "query": { "match_all": {} },
  "size": 1
}

>>Note that if size is not specified, it defaults to 10.

>>This example does a match_all and returns documents 10 through 19:
GET /bank/_search
{
  "query": { "match_all": {} },
  "from": 10,
  "size": 10
}

>> This example does a match_all and sorts the results by account balance in descending order and returns the top 10 (default size) documents.

GET /bank/_search
{
  "query": { "match_all": {} },
  "sort": { "balance": { "order": "desc" } }
}

>>  By default, the full JSON document is returned as part of all searches. This is referred to as the source (_source field in the search hits). 
If we don’t want the entire source document returned, we have the ability to request only a few fields from within source to be returned.
This example shows how to return two fields, account_number and balance (inside of _source), from the search:
GET /bank/_search
{
  "query": { "match_all": {} },
  "_source": ["account_number", "balance"]
}

Note that the above example simply reduces the _source field. It will still only return one field named _source but within it,
 only the fields account_number and balance are included.

 >>Let’s now introduce a new query called the match query, which can be thought of as a basic fielded search query (i.e. a search done against a specific field or set of fields).
This example returns the account numbered 20:
GET /bank/_search
{
  "query": { "match": { "account_number": 20 } }
}

>> This example returns all accounts containing the term "mill" in the address:
GET /bank/_search
{
  "query": { "match": { "address": "mill" } }
}

>> This example returns all accounts containing the term "mill" or "lane" in the address:
GET /bank/_search
{
  "query": { "match": { "address": "mill lane" } }
}

>> This example is a variant of match (match_phrase) that returns all accounts containing the phrase "mill lane" in the address:

GET /bank/_search
{
  "query": { "match_phrase": { "address": "mill lane" } }
}

>>Let’s now introduce the bool query. The bool query allows us to compose smaller queries into bigger queries using boolean logic.

This example composes two match queries and returns all accounts containing "mill" and "lane" in the address:
GET /bank/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "address": "mill" } },
        { "match": { "address": "lane" } }
      ]
    }
  }
}

In the above example, the bool must clause specifies all the queries that must be true for a document to be considered a match.

>>In contrast, this example composes two match queries and returns all accounts containing "mill" or "lane" in the address:

GET /bank/_search
{
  "query": {
    "bool": {
      "should": [
        { "match": { "address": "mill" } },
        { "match": { "address": "lane" } }
      ]
    }
  }
}

In the above example, the bool should clause specifies a list of queries either of which must be true for a document to be considered a match.

>>This example composes two match queries and returns all accounts that contain neither "mill" nor "lane" in the address:

GET /bank/_search
{
  "query": {
    "bool": {
      "must_not": [
        { "match": { "address": "mill" } },
        { "match": { "address": "lane" } }
      ]
    }
  }
}

>>This example returns all accounts of anybody who is 40 years old but doesn’t live in ID(aho):

GET /bank/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "age": "40" } }
      ],
      "must_not": [
        { "match": { "state": "ID" } }
      ]
    }
  }
}

==================================================================================================
>> executing filters :- 

The score is a numeric value that is a relative measure of how well the document matches the search query that we specified
The higher the score, the more relevant the document is, the lower the score, the less relevant the document is.

let’s introduce the range query, which allows us to filter documents by a range of values. This is generally used for numeric or date filtering.

This example uses a bool query to return all accounts with balances between 20000 and 30000, inclusive. In other words, we want to find accounts with
 a balance that is greater than or equal to 20000 and less than or equal to 30000.
GET /bank/_search
{
  "query": {
    "bool": {
      "must": { "match_all": {} },
      "filter": {
        "range": {
          "balance": {
            "gte": 20000,
            "lte": 30000
          }
        }
      }
    }
  }
}

=================================================================================================
executing aggregation :- 

Aggregations provide the ability to group and extract statistics from your data. The easiest way to think about aggregations is by roughly equating
 it to the SQL GROUP BY and the SQL aggregate functions.
To start with, this example groups all the accounts by state, and then returns the top 10 (default) states sorted by count descending (also default):

GET /bank/_search
{
  "size": 0,
  "aggs": {
    "group_by_state": {
      "terms": {
        "field": "state.keyword"
      }
    }
  }
}


>> this example calculates the average account balance by state (again only for the top 10 states sorted by count in descending order):
GET /bank/_search
	{
	  "size": 0,
	  "aggs": {
		"group_by_state": {
		  "terms": {
			"field": "state.keyword"
		  },
		  "aggs": {
			"average_balance": {
			  "avg": {
				"field": "balance"
			  }
			}
		  }
		}
	  }
	}

>> Building on the previous aggregation, let’s now sort on the average balance in descending order:
GET /bank/_search
{
  "size": 0,
  "aggs": {
    "group_by_state": {
      "terms": {
        "field": "state.keyword",
        "order": {
          "average_balance": "desc"
        }
      },
      "aggs": {
        "average_balance": {
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  }
}

===============================================================================
check if elastic search is running :- 
GET /

output :-  
{
  "name": "gqjOnhb",
  "cluster_name": "elasticsearch",
  "cluster_uuid": "JTi0nW1rQ9CM99AEV4T7iQ",
  "version": {
    "number": "6.2.2",
    "build_hash": "10b1edd",
    "build_date": "2018-02-16T19:01:30.685723Z",
    "build_snapshot": false,
    "lucene_version": "7.2.1",
    "minimum_wire_compatibility_version": "5.6.0",
    "minimum_index_compatibility_version": "5.0.0"
  },
  "tagline": "You Know, for Search"
}

 
 >> for running elastic search as a daemon :- 
 ./bin/elasticsearch -d -p pid
 
 ====================================================================
 elastic search is running at- http://localhost:9200 
 kebana is running at - http://localhost:5601/app/kibana#/dev_tools/console?_g=()
 http://localhost:9200/bank/_search
 
 GET /_cat/indices?v
 GET /accounts/_search?q=*
 
 
 
 

 
