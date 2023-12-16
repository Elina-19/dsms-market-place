### PostgreSQL
create table account
(
    id                uuid not null
        constraint pk_account primary key,
    create_date       timestamp default now(),
    first_name        varchar(255) not null,
    last_name         varchar(255) not null,
    email             varchar(255)
        constraint uc_account_email unique,
    password          varchar(255)
);

## ClickHouse
create table item_visit
(
    createDate         DateTime,
    userId             String,
    itemId             String,
    categoryId         String,
    categoryName       String,
    fromRecommendation Bool
) engine = MergeTree()
ORDER BY (categoryId, categoryName)
SETTINGS index_granularity = 8192;

create table item_visit_kafka_in
(
    createDate         DateTime,
    userId             String,
    itemId             String,
    categoryId         String,
    categoryName       String,
    fromRecommendation Bool
) engine = Kafka()
SETTINGS kafka_broker_list = 'kafka:9092', kafka_topic_list = 'items.visit',
    kafka_group_name = 'dsms', kafka_format = 'JSONEachRow';

create materialized view item_visit_mv to item_visit (
    `createDate`         DateTime,
    `userId`             String,
    `itemId`             String,
    `categoryId`         String,
    `categoryName`       String,
    `fromRecommendation` Bool
)
as select * from item_visit_kafka_in;

-- drop table item_visit_kafka_in;
-- drop view item_visit_mv;

-----

create table item_to_cart
(
    createDate         DateTime,
    userId             String,
    itemId             String,
    fromRecommendation Bool,
    isAdded            Bool
) engine = MergeTree()
ORDER BY (itemId)
SETTINGS index_granularity = 8192;

create table item_to_cart_kafka_in
(
    createDate         DateTime,
    userId             String,
    itemId             String,
    fromRecommendation Bool,
    isAdded            Bool
) engine = Kafka()
SETTINGS kafka_broker_list = 'kafka:9092', kafka_topic_list = 'items.to.cart',
    kafka_group_name = 'dsms', kafka_format = 'JSONEachRow';

create materialized view item_to_cart_mv to item_to_cart (
    `createDate`         DateTime,
    `userId`             String,
    `itemId`             String,
    `fromRecommendation` Bool,
    `isAdded`            Bool
)
as select * from item_to_cart_kafka_in;

-----

create table support_service_request
(
    createDate              DateTime,
    userId                  String,
    itemId                  String,
    communicationSourceId   String,
    communicationSourceName String
) engine = MergeTree()
ORDER BY (communicationSourceId, communicationSourceName)
SETTINGS index_granularity = 8192;

create table support_service_request_kafka_in
(
    createDate              DateTime,
    userId                  String,
    itemId                  String,
    communicationSourceId   String,
    communicationSourceName String
) engine = Kafka()
SETTINGS kafka_broker_list = 'kafka:9092', kafka_topic_list = 'support.service.requests',
    kafka_group_name = 'dsms', kafka_format = 'JSONEachRow';

create materialized view support_service_request_mv to support_service_request (
    `createDate`              DateTime,
    `userId`                  String,
    `itemId`                  String,
    `communicationSourceId`   String,
    `communicationSourceName` String
)
as select * from support_service_request_kafka_in;
