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
curl -X POST localhost:8080/api/content/categories/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"name":"kitchen"}' > temp.log
# get all categories
curl -X GET localhost:8080/api/content/categories/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
# modify category
curl -X PUT localhost:8080/api/content/categories/1 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"name":"kitchenware"}' > temp.log
# get one specific category
curl -X GET localhost:8080/api/content/categories/1 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
