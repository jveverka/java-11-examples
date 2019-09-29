# ElasticSearch cheat sheet

## Data Object
```
{
   "id": {
      "id": "unique-object-id"
   },
   "name": "event-name",
   "description": "event-description",
   "related": [
      { "id": "other-event-id-01" },
      { "id": "other-event-id-01" }
   ],
   "geoLocation": {
       "longitude": 42.154,
       "latitude": 52.152
   },
   "timeStamp": 1357964654 
}
```

## Indexes
* list indexes
  ``GET http://localhost:9200/_cat/indices?format=json&pretty=true``
* get all mappings
  ``GET http://localhost:9200/_mapping?format=json&pretty=true`` 
* create index 'testindex'
  ``PUT http://localhost:9200/testindex``
  ```
  {
      "mappings": {
        "properties": {
          "description": {
            "type": "text"
          },
          "geoLocation": {
            "type": "geo_point"
          },
          "id": {
            "type": "nested",
            "properties": {
              "id": {
                "type": "keyword"
              }
            }
          },
          "name": {
            "type": "text"
          },
          "related": {
            "type": "nested",
            "properties": {
              "id": {
                "type": "keyword"
              }
            }
          },
          "timeStamp": {
            "type": "date"
          }
        }
      }
  }
  ```
* get mapping for index 'testindex'
  ``GET http://localhost:9200/testindex/_mapping?format=json&pretty=true``   
* delete index 'testindex'
  ``DELETE http://localhost:9200/testindex``

## Documents

## Queries