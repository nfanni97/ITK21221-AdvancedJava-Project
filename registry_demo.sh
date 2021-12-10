# create user2 with registry
echo "Creating user2"
curl -s -X POST localhost:8080/api/auth/signup -H "Content-Type: application/json" -d '{"username":"user2","password":"123"}' > temp.log
curl -s -X POST localhost:8080/api/auth/signin -H "Content-Type: application/json" -d '{"username":"user2","password":"123"}' > temp.log
export USER2_TOKEN=$(cat temp.log | jq -r '.token')
export USER2_ID=$(cat temp.log | jq -r '.id')
read -n 1

# at first it is empty
curl -s -X GET localhost:8080/api/content/registries/registry/$USER2_ID -H "Content-Type: application/json" -H "Authorization: Bearer $USER2_TOKEN" > temp.log

# add products to it
echo "Add products to registry of user2..."
curl -s -X POST localhost:8080/api/content/registries/add -H "Content-Type: application/json" -H "Authorization: Bearer $USER2_TOKEN" -d '{"productId":1}' > temp.log
curl -s -X POST localhost:8080/api/content/registries/add -H "Content-Type: application/json" -H "Authorization: Bearer $USER2_TOKEN" -d '{"productId":3}' > temp.log
echo "This endpoint returns whole registry"
read -n 1

# create another user
echo "Creating user3"
curl -s -X POST localhost:8080/api/auth/signup -H "Content-Type: application/json" -d '{"username":"user3","password":"123"}' > temp.log
curl -s -X POST localhost:8080/api/auth/signin -H "Content-Type: application/json" -d '{"username":"user3","password":"123"}' > temp.log
export USER3_TOKEN=$(cat temp.log | jq -r '.token')
export USER3_ID=$(cat temp.log | jq -r '.id')

# buy china vase for user2 by user3
echo "User3 buys the china vase for user2"
curl -s -X POST localhost:8080/api/content/registries/buy -H "Content-Type: application/json" -H "Authorization: Bearer $USER3_TOKEN" -d '{"productId":1,"recipientId":'$USER2_ID'}' > temp.log
echo "Returns buyResponse (to whom, what)"
read -n 1

# get everything bought by user3
echo "Get everything that user3 bought"
curl -s -X GET localhost:8080/api/content/registries/i-bought -H "Content-Type: application/json" -H "Authorization: Bearer $USER3_TOKEN" > temp.log
echo "returns products grouped by recipient"
read -n 1

# get items bought for user2
echo "Get items bought for user2 with known buyers..."
curl -s -X GET localhost:8080/api/content/registries/bought-for-me -H "Content-Type: application/json" -H "Authorization: Bearer $USER2_TOKEN" > temp.log
read -n 1
echo "...and buyer names obfuscated"
curl -s -X GET localhost:8080/api/content/registries/bought-for-me/surprise -H "Content-Type: application/json" -H "Authorization: Bearer $USER2_TOKEN" > temp.log
read -n 1

# get all registries
echo "Get all registries to browse"
curl -s -X GET localhost:8080/api/content/registries/all -H "Content-Type: application/json" -H "Authorization: Bearer $USER3_TOKEN" > temp.log
read -n 1

# unbuy china vase for user2 by user3
echo "user3 changed their mind"
curl -s -X DELETE localhost:8080/api/content/registries/unbuy -H "Content-Type: application/json" -H "Authorization: Bearer $USER3_TOKEN" -d '{"productId":1,"recipientId":'$USER2_ID'}' > temp.log
read -n 1

echo "Look at logging"
read -n 1

echo "Look at tests"
read -n 1

echo "Finished :)"
