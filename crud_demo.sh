# sign up user1
curl -X POST localhost:8080/api/auth/signup -H "Content-Type: application/json" -d '{"username":"user1","password":"123"}'

# log in user1
curl -X POST localhost:8080/api/auth/signin -H "Content-Type: application/json" -d '{"username":"user1","password":"123"}' > temp.log
# save token
export TOKEN=$(cat temp.log | jq -r '.token')

#  dummy content
curl -X GET localhost:8080/api/content/hello -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
echo "Content on the endpoint /api/content/hello for user1: $(cat temp.log)"

# categories endpoint
# add category
curl -X POST localhost:8080/api/content/categories/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"name":"kitchenware"}' > temp.log
# get all categories
curl -X GET localhost:8080/api/content/categories/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
# modify category
curl -X PUT localhost:8080/api/content/categories/1 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"name":"kitchen"}' > temp.log
# get one specific category
curl -X GET localhost:8080/api/content/categories/1 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
# delete a category
curl -X DELETE localhost:8080/api/content/categories/5 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN"
curl -X GET localhost:8080/api/content/categories/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log

# products endpoint
# add product
curl -X POST localhost:8080/api/content/products/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"name":"kitchen clock","priceHuf":5990,"categories":["kitchen","decoration"]}' > temp.log
# get all products
curl -X GET localhost:8080/api/content/products/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
# modify product
curl -X PUT localhost:8080/api/content/products/2 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"name":"kitchen clock","priceHuf":6600,"categories":["kitchen"]}' > temp.log
# get one specific product
curl -X GET localhost:8080/api/content/products/2 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
# delete a product
curl -X DELETE localhost:8080/api/content/products/2 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN"
curl -X GET localhost:8080/api/content/products/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
