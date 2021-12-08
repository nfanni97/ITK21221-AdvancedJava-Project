# create user2 with registry
curl -X POST localhost:8080/api/auth/signup -H "Content-Type: application/json" -d '{"username":"user2","password":"123"}'
curl -X POST localhost:8080/api/auth/signin -H "Content-Type: application/json" -d '{"username":"user2","password":"123"}' > temp.log
export USER2_TOKEN=$(cat temp.log | jq -r '.token')
export USER2_ID=$(cat temp.log | jq -r '.id')

# at first it is empty
curl -X GET localhost:8080/api/content/registries/registry/$USER2_ID -H "Content-Type: application/json" -H "Authorization: Bearer $USER2_TOKEN" > temp.log

# add a product to it
curl -X POST localhost:8080/api/content/registries/add -H "Content-Type: application/json" -H "Authorization: Bearer $USER2_TOKEN" -d '{"productId":1}' > temp.log

# create another user
curl -X POST localhost:8080/api/auth/signup -H "Content-Type: application/json" -d '{"username":"user3","password":"123"}'
curl -X POST localhost:8080/api/auth/signin -H "Content-Type: application/json" -d '{"username":"user3","password":"123"}' > temp.log
export USER3_TOKEN=$(cat temp.log | jq -r '.token')
export USER3_ID=$(cat temp.log | jq -r '.id')

# buy china vase for user2 by user3
curl -X POST localhost:8080/api/content/registries/buy -H "Content-Type: application/json" -H "Authorization: Bearer $USER3_TOKEN" -d '{"productId":1,"recipientId":'$USER2_ID'}' > temp.log

# get everything bought by user3
curl -X GET localhost:8080/api/content/registries/i-bought -H "Content-Type: application/json" -H "Authorization: Bearer $USER3_TOKEN" > temp.log
read -n 1

# get all registries
curl -X GET localhost:8080/api/content/registries/all -H "Content-Type: application/json" -H "Authorization: Bearer $USER3_TOKEN" > temp.log

# unbuy china vase for user2 by user3
curl -X DELETE localhost:8080/api/content/registries/unbuy -H "Content-Type: application/json" -H "Authorization: Bearer $USER3_TOKEN" -d '{"productId":1,"recipientId":'$USER2_ID'}' > temp.log