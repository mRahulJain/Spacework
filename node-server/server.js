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

    //START WEB SERVER
    // var port = process.env.PORT || 3000;
    var port = 3000;
    Promise.resolve(app.listen(port)).then(()=>{
      console.log("Connected to mongodb server, web server is running on port 3000");
    });
  }
});
