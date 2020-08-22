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
    //ADD PRODUCT
    app.post('/add_product', (request, response)=>{
      var postData = request.body;

      var productId = `${postData.productId}`;
      var productName = `${postData.productName}`;
      var productDescription = `${postData.productDescription}`;
      var productPrice = `${postData.productPrice}`;
      var productIsAvailable = `${postData.productIsAvailable}`;
      var productImage = `${postData.productImage}`;

      var insertJson = {
        productId: productId,
        productName: productName,
        productDescription: productDescription,
        productPrice: productPrice,
        productIsAvailable: productIsAvailable,
        productImage: productImage
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


    //START WEB SERVER
    // var port = process.env.PORT || 3000;
    var port = 3000;
    Promise.resolve(app.listen(port)).then(()=>{
      console.log("Connected to mongodb server, web server is running on port 3000");
    });
  }
});
