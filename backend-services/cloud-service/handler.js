'use strict';

var AWS = require("aws-sdk");

module.exports.hello = async (event) => {

  AWS.config.update({
    region: "us-east-1"
  });

  var ddb = new AWS.DynamoDB({apiVersion: '2012-08-10'});
  var myTable = 'insuranceTable';
  try{

    var xy=event["queryStringParameters"]["xy"];
    var yz=event["queryStringParameters"]["yz"];
    var zx=event["queryStringParameters"]["zx"];
    var impactVal=(parseFloat(xy)+parseFloat(yz)+parseFloat(zx)).toString();

    var params = {
      TableName: myTable,
      Item: {'carNumber' : {S: '100329'}, 'xy' : {S: xy}, 'yz' : {S: yz}, 'zx' : {S: zx}, 'impactValue' : {S: impactVal}}
    };
  
    var paramsValue = await post(ddb,params);
    console.log(paramsValue);
  

    return {
      statusCode: 200,
      body: JSON.stringify({
        message: 'Impact Values sent to Insurance Company Cloud',
        xy,yz,zx,impactValue:parseFloat(parseFloat(xy)+parseFloat(yz)+parseFloat(zx))
      }),
    };
  }catch(e){
    return {
      statusCode: 500,
      body: JSON.stringify({
        "errorMessage":e
      }),
    };
  } 
};

var post = function (ddb,params) {
  return new Promise(function(resolve,reject){

    ddb.putItem(params, function(err, data) {
      if (err) {
        console.log("Error", err);
        reject(err);
      } else {
        console.log("Success", data);
        resolve("success")
      }
    });

  })
 
}





