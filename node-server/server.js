var mongodb = require('mongodb');
var objectId = mongodb.ObjectID;
var express = require('express');
var bodyParser = require('body-parser');

//CREATE EXPRESS SERVICE
var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

//CREATE MONGODB CLIENT
var mongoClient = mongodb.MongoClient;

//CONNECTION URL
var url = "mongodb://localhost:27017";
mongoClient.connect(url, {useNewUrlParser: true}, function(err,client) {
  if(err) {
    console.log("Unable to connect to the mongodb server. Error: ", err);
  } else {

    //--------------------------------------------------------------------------------------------------------//
    //POST PRODUCT
    app.post('/add-product', (request, response)=>{
      var postData = request.body;

      var productId = `${postData.productId}`;
      var productName = `${postData.productName}`;
      var productDescription = `${postData.productDescription}`;
      var productPrice = `${postData.productPrice}`;
      var productIsAvailable = `${postData.productIsAvailable}`;
      var productImage = `${postData.productImage}`;
      var productCategory = `${postData.productCategory}`;

      var insertJson = {
        productId: productId,
        productName: productName,
        productDescription: productDescription,
        productPrice: productPrice,
        productIsAvailable: productIsAvailable,
        productImage: productImage,
        productCategory: productCategory
      };

      var db = client.db('spacework');

      db.collection('products').find({'productId': productId}).count(function(err,number){
        if(number!=0) {
          response.json('Product already exists');
          console.log('Product already exists');
        } else {
          db.collection('products').insertOne(insertJson,function(err,res){
            response.json('Product added');
            console.log('Product added');
          })
        }
      })
    });
    //--------------------------------------------------------------------------------------------------------//


    //--------------------------------------------------------------------------------------------------------//
    //GET PRODUCT BY CATEGORY
    app.get('/get-product-by-category', (request, response)=>{
      var queryData = request.query;
      var productCategory = `${queryData.productCategory}`;

      var db = client.db('spacework');
      db.collection('products').find({'productCategory': productCategory}).count(function(err,number){
        if(number==0) {
          response.json();
          console.log('No such category exists');
        } else {
          db.collection('products').find({'productCategory': productCategory}).toArray(function(err,items){
            response.json(items);
            console.log('Success in getting products by category');
          })
        }
      })
    });
    //--------------------------------------------------------------------------------------------------------//


    //--------------------------------------------------------------------------------------------------------//
    //GET PRODUCT BY ID
    app.get('/get-product-by-id', (request, response)=>{
      var queryData = request.query;
      var productId = `${queryData.productId}`;

      var db = client.db('spacework');
      db.collection('products').find({'productId': productId}).count(function(err,number){
        if(number==0) {
          response.json();
          console.log('No such product exists');
        } else {
          db.collection('products').findOne({'productId': productId}, function(err,res){
            response.json(res);
            console.log('Success in getting product by id');
          })
        }
      })
    });
    //--------------------------------------------------------------------------------------------------------//


    //--------------------------------------------------------------------------------------------------------//
    //PATCH PRODUCT
    app.patch('/update-product', (request, response)=>{
      var patchData = request.body;

      var productName = `${patchData.productName}`;
      var productDescription = `${patchData.productDescription}`;
      var productPrice = `${patchData.productPrice}`;
      var productIsAvailable = `${patchData.productIsAvailable}`;
      var productImage = `${patchData.productImage}`;

      var db = client.db('spacework');
      db.collection('products').updateOne(
        {
          'productName': productName
        },
          {
            $set:{
              'productDescription': productDescription,
              'productPrice': productPrice,
              'productIsAvailable': productIsAvailable,
              'productImage': productImage
            }
          },
          function(err,res){
            response.json("Product Updated");
            console.log("Product Updated");
          }
        )
      });
      //--------------------------------------------------------------------------------------------------------//


      //--------------------------------------------------------------------------------------------------------//
      //DELETE PRODUCT
      app.post("/delete-product",(request, response)=>{
        var deleteData = request.body;

        var productId = `${deleteData.productId}`;

        var db = client.db('spacework');
        db.collection('products').find({'productId': productId}).count(function(err,number){
          if(number == 0) {
            response.json("Product does not exist");
            console.log("Product does not exist");
          } else {
            db.collection('products').deleteOne({'productId': productId}, function(err,res){
              response.json("Delete Successfull");
              console.log("Delete Successfull");
            })
          }
        })
      });
      //--------------------------------------------------------------------------------------------------------//


      //--------------------------------------------------------------------------------------------------------//
      //GET ALL PRODUCTS
      function shuffle(array) {
         var currentIndex = array.length
          , temporaryValue
          , randomIndex
          ;

        while (0 !== currentIndex) {
          randomIndex = Math.floor(Math.random() * currentIndex);
          currentIndex -= 1;

          temporaryValue = array[currentIndex];
          array[currentIndex] = array[randomIndex];
          array[randomIndex] = temporaryValue;
        }

        return array;
      }
      app.get("/get-all-products",(request, response)=>{
        var db = client.db('spacework');
        db.collection('products').find().count(function(err,number){
          if(number==0) {
            response.json();
            console.log('No such category exists');
          } else {
            db.collection('products').find().toArray(function(err,items){
              response.json(shuffle(items));
              console.log('Success in getting products');
            })
          }
        })
      });
      //--------------------------------------------------------------------------------------------------------//


      //--------------------------------------------------------------------------------------------------------//
      //POST USER
      app.post("/add-user", (request,response)=>{
        var postData = request.body;

        var userPhoneNumber = `${postData.userPhoneNumber}`;
        var userName = `${postData.userName}`;
        var userAddress = `${postData.userAddress}`;
        var userToken = `${postData.userToken}`;

        var insertJson = {
          userPhoneNumber: userPhoneNumber,
          userName: userName,
          userAddress: userAddress,
          userToken: userToken
        };

        var db = client.db('spacework');

        db.collection('users').find({'userPhoneNumber': userPhoneNumber}).count(function(err,number){
          if(number!=0) {
            db.collection('users').deleteOne({'userPhoneNumber': userPhoneNumber}, function(err,res){
              db.collection('users').insertOne(insertJson,function(err,res){
                response.json('User added');
                console.log('User added');
              })
            })
          } else {
            db.collection('users').insertOne(insertJson,function(err,res){
              response.json('User added');
              console.log('User added');
            })
          }
        })
      })
      //--------------------------------------------------------------------------------------------------------//


      //--------------------------------------------------------------------------------------------------------//
      //PATCH USER
      app.patch('/update-user', (request, response)=>{
        var patchData = request.body;

        var userPhoneNumber = `${patchData.userPhoneNumber}`;
        var userAddress = `${patchData.userAddress}`;
        var userName = `${patchData.userName}`;

        var db = client.db('spacework');
        db.collection('users').updateOne(
          {
            'userPhoneNumber': userPhoneNumber
          },
            {
              $set:{
                'userName': userName,
                'userAddress': userAddress
              }
            },
            function(err,res){
              response.json("User Updated");
              console.log("User Updated");
            }
          )
        });
        //--------------------------------------------------------------------------------------------------------//


        //--------------------------------------------------------------------------------------------------------//
        //POST ORDERS
        app.post("/post-order",(request, response)=> {
          var postData = request.body;

          var userPhoneNumber = `${postData.userPhoneNumber}`;
          var userName = `${postData.userName}`;
          var userAddress = `${postData.userAddress}`;
          var orderHashmap = `${postData.orderHashmap}`;
          var orderDate = new Date().toISOString();
          var orderStatus = 'Order Received';
          var orderHasPaid = 'no';
          var orderTotal = `${postData.orderTotal}`;

          var userOrder = {
            orderHashmap: orderHashmap,
            orderStatus: orderStatus,
            orderDate: orderDate,
            orderTotal: orderTotal,
            orderHasPaid: orderHasPaid
          };

          var db = client.db('spacework');
          db.collection('orders').find({'userPhoneNumber': userPhoneNumber}).count(function(err,number){
            if(number == 0) {
              var userOrders = [userOrder];
              var insertJson = {
                userPhoneNumber: userPhoneNumber,
                userName: userName,
                userAddress: userAddress,
                userOrders: userOrders
              };

              db.collection('orders').insertOne(insertJson, function(err,res){
                response.json("Order Placed");
                console.log("Order Placed");
              })
            } else {
              db.collection('orders').updateOne(
                {'userPhoneNumber': userPhoneNumber},
                {$push: {'userOrders': userOrder}},
                function(err,res){
                  response.json("Order Placed");
                  console.log("Order Placed");
                }
              )
            }
          })
        });
        //--------------------------------------------------------------------------------------------------------//



        //--------------------------------------------------------------------------------------------------------//
        //GET ORDERS BY USERID
        app.get("/get-order-by-userid",(request, response)=> {
          var queryData = request.query;

          var userPhoneNumber = `+${queryData.userPhoneNumber}`;
          console.log(userPhoneNumber);
          var db = client.db('spacework');
          db.collection('orders').find({'userPhoneNumber': userPhoneNumber}).count(function(err,number){
            if(number == 0) {
              response.json();
              console.log('No orders present by this user');
            } else {
              db.collection('orders').findOne(
                {'userPhoneNumber': userPhoneNumber},
                function(err,res) {
                  var toSendArray = res.userOrders;
                  response.json(toSendArray);
                  console.log("Successfull in getting orders");
                }
              )
            }
          })
        });
        //--------------------------------------------------------------------------------------------------------//



        //--------------------------------------------------------------------------------------------------------//
        //GET ALL ORDERS
        app.get("/get-all-orders",(request, response)=> {
          var db = client.db('spacework');
          db.collection('orders').find().count(function(err,number){
            if(number == 0) {
              response.json();
              console.log('No orders present by this user');
            } else {
              db.collection('orders').find().toArray(function(err,items){
                response.json(shuffle(items));
                console.log('Success in getting orders');
              })
            }
          })
        });
        //--------------------------------------------------------------------------------------------------------//


        //--------------------------------------------------------------------------------------------------------//
        //PATCH ORDERS BY USERID CHANGE ORDERHASPAID
        app.patch("/order-by-id-haspaid",(request, response)=> {
          var patchData = request.body;

          var userPhoneNumber = `+${patchData.userPhoneNumber}`;
          var orderHashmap = `${patchData.orderHashmap}`;
          var orderHasPaid = `${patchData.orderHasPaid}`;

          var db = client.db('spacework');
          db.collection('orders').find({'userPhoneNumber': userPhoneNumber}).count(function(err,number){
            if(number == 0) {
              response.json();
              console.log('No orders present by this user');
            } else {
              db.collection('orders').updateOne(
                {'userOrders': {$elemMatch: {'orderHashmap': orderHashmap}}},
                {$set: {'userOrders.$.orderHasPaid': orderHasPaid}},
                function(err,res) {
                  response.json("Paid status updated");
                  console.log("Paid status updated");
                }
              )
            }
          })
        });
        //--------------------------------------------------------------------------------------------------------//



        //--------------------------------------------------------------------------------------------------------//
        //PATCH ORDERS BY USERID CHANGE ORDERDELIVERY
        app.patch("/order-by-id-delivery",(request, response)=> {
          var patchData = request.body;

          var userPhoneNumber = `+${patchData.userPhoneNumber}`;
          var orderHashmap = `${patchData.orderHashmap}`;
          var orderStatus = `${patchData.orderStatus}`;

          var db = client.db('spacework');
          db.collection('orders').find({'userPhoneNumber': userPhoneNumber}).count(function(err,number){
            if(number == 0) {
              response.json();
              console.log('No orders present by this user');
            } else {
              db.collection('orders').updateOne(
                {'userOrders': {$elemMatch: {'orderHashmap': orderHashmap}}},
                {$set: {'userOrders.$.orderStatus': orderStatus}},
                function(err,res) {
                  response.json("Delivery status updated");
                  console.log("Delivery status updated");
                }
              )
            }
          })
        });
        //--------------------------------------------------------------------------------------------------------//



        //--------------------------------------------------------------------------------------------------------//
        //PATCH ORDERS BY USERID CANCEL ORDER
        app.patch("/cancel-order",(request, response)=> {
          var patchData = request.body;

          var userPhoneNumber = `+${patchData.userPhoneNumber}`;
          var orderHashmap = `${patchData.orderHashmap}`;

          var db = client.db('spacework');
          db.collection('orders').find({'userPhoneNumber': userPhoneNumber}).count(function(err,number){
            if(number == 0) {
              response.json();
              console.log('No orders present by this user');
            } else {
              db.collection('orders').updateOne(
                {'userOrders': {$elemMatch: {'orderHashmap': orderHashmap}}},
                {$pull: {'userOrders': {'orderHashmap': orderHashmap}}},
                function(err,res) {
                  response.json("Ordered Cancelled");
                  console.log("Ordered Cancelled");
                }
              )
            }
          })
        });
        //--------------------------------------------------------------------------------------------------------//


    //START WEB SERVER
    var port = process.env.PORT || 3000;
    //var port = 3000;
    Promise.resolve(app.listen(port)).then(()=>{
      console.log("Connected to mongodb server, web server is running on port 3000");
    });
  }
});
