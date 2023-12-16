# Количество зарегистрированных пользователей за день с агрегацией по дням

SELECT DATE(create_date) AS date, COUNT(*) AS users_count
FROM account
WHERE create_date > (now() - interval '30 days')
GROUP BY date;

---

# Количество просмотров на страницах товаров с агрегацией по категориям по дням

select toDate(createDate) as time, categoryId, categoryName, count(*) from item_visit
group by time, categoryId, categoryName;

---

# Анализ средства обращения в службу поддержки, чат, email или телефон

select count(*) as count from support_service_request where communicationSourceId = '1';
select count(*) as count from support_service_request where communicationSourceId = '2';
select count(*) as count from support_service_request where communicationSourceId = '3';

---

# Количество переходов на страницы товаров из ленты с рекомендациями

select toDate(createDate) as time, count(*) from item_visit
where fromRecommendation = true
group by time;

---

# Добавление товара в корзину из ленты с рекомендациями

select toDate(createDate) as time, count(*) from item_to_cart
where fromRecommendation = true and isAdded = true
group by time;
