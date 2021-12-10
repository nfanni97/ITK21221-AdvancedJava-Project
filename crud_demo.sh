# sign up user1
echo "Signing up user1..."
curl -s -X POST localhost:8080/api/auth/signup -H "Content-Type: application/json" -d '{"username":"user1","password":"123"}' > temp.log

# log in user1
echo "Logging in user1..."
curl -s -X POST localhost:8080/api/auth/signin -H "Content-Type: application/json" -d '{"username":"user1","password":"123"}' > temp.log
# save token
export TOKEN=$(cat temp.log | jq -r '.token')

#  dummy content
#curl -s -X GET localhost:8080/api/content/hello -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
#echo "Content on the endpoint /api/content/hello for user1: $(cat temp.log)"

# categories endpoint
# add category
echo "Adding category with name kitchenware..."
curl -s -X POST localhost:8080/api/content/categories/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"name":"kitchenware"}' > temp.log
read -n 1
# get all categories
echo "Getting all categories..."
curl -s -X GET localhost:8080/api/content/categories/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
read -n 1
# modify category
echo "Modifying category with id 1 to have the name kitchen..."
curl -s -X PUT localhost:8080/api/content/categories/1 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"name":"kitchen"}' > temp.log
read -n 1
# get one specific category
echo "Get only the category with id 1..."
curl -s -X GET localhost:8080/api/content/categories/1 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
read -n 1
# delete a category
echo "Deleting category with id 5 (doesn't exist!)..."
curl -s -X DELETE localhost:8080/api/content/categories/5 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
read -n 1
echo "Deleting category with id 3..."
curl -s -X DELETE localhost:8080/api/content/categories/3 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN"
curl -s -X GET localhost:8080/api/content/categories/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
read -n 1

# products endpoint
# add product
curl -s -X POST localhost:8080/api/content/products/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"name":"kitchen clock","priceHuf":5990,"categories":["kitchen","decoration"]}' > temp.log
# get all products
curl -s -X GET localhost:8080/api/content/products/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
# modify product
curl -s -X PUT localhost:8080/api/content/products/2 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"name":"kitchen clock","priceHuf":6600,"categories":["kitchen"]}' > temp.log
# get one specific product
curl -s -X GET localhost:8080/api/content/products/2 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log
# delete a product
curl -s -X DELETE localhost:8080/api/content/products/2 -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN"
curl -s -X GET localhost:8080/api/content/products/ -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" > temp.log

echo "Did the same for products too, last request was to get all products (note price in EUR)"
read -n 1
