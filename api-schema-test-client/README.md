# Background #


In the cartrawler platform, (taxi operators) supplier can be
integrated in many ways and one of them is supplierApi where supplier
build the system by following the instruction provided by cartrawler.

Contact: gtdevsupport@cartrawler.com for  more information.

# Purpose #


* The purpose of the tests are to guide/help supplier integrating the system.
* The tests are made to verify request and response of each required api pass json validation.
* No communication to cartrawler is made as it is a mere simulation of how cartrawler system will behave after the integration.


# Structure #

All the files are requested to place as it is.

Test are divided as given below

### Test 1 ###

Search Test (/v1/pricing/search)

### Test 2 ###

Create Test (/v1/booking/create)
  - search
  - create

### Test 3 ###

Cancel Test (/v1/booking/cancel)

### Test 4 ###

Status Test (/v1/booking/status)

### Test ###

- Complete Test
  - search
  - create
  - status
  - cancel
  - status

### Test 6 ###

Performance Test
  - search

## Required Setting ##

config.json holds the necessary configuration keys and values

baseUrl: <suppliers system url>
authKey: key provided by cartrawler
searchDataGenericProduct :  address information for the search
bookingCreateData: address and passenger information for creating a booking

### Requirement ###

ruby 2.3.1p112 or newer

#### gem dependencies ####
require "json-schema"

ruby installation guide (https://www.ruby-lang.org/en/documentation/installation/)

How to Run

using test.sh

```
./test.sh <v1> <v2> e.g.
./test.sh 1 : performing search test
./test.sh 2 : performing create test
./test.sh 3 6aef2eb29e89eedb912276b159f237d2 : performing cancel test
./test.sh 4 6aef2eb29e89eedb912276b159f237d2 : performing status test
./test.sh 5 : performing complete test
./test.sh 6 2 : performing performance test with 2 thread in a second
```

#### using test.rb ####

ruby dependencyChecker.rb (required at least a hit)

ruby test.rb <v1> <v2> e.g.
ruby test.rb 1 : performing search test
ruby test.rb 2 : performing create test
ruby test.rb 3 6aef2eb29e89eedb912276b159f237d2 : performing cancel test
ruby test.rb 4 6aef2eb29e89eedb912276b159f237d2 : performing status test
ruby test.rb 5 : performing complete test
ruby test.rb 6 2 : performing performance test with 2 thread in a second


### Outputs ###

./test.sh 1

Search Request  Schema Validataion = true
received 5 offers
Search Response  Schema Validataion = true

./test.sh 2

Search Request  Schema Validataion = true
received 5 offers

Search Response  Schema Validataion = true
now selecting one of the offer and creating a booking

data used for creating booking

```
{"bookingRefId"=>"afbb086daa16fc97e0d70a221c281a96", "shortId"=>"465927485", "supplierPricingRefId"=>"PR-27401101", "pickupDateTime"=>"2017-10-07T13:53", "addresses"=>[{"address"=>"De Hoef Westzijde 33, 1426 AT De Hoef, Netherlands", "category"=>"Address", "location"=>{"lat"=>52.2032, "lng"=>4.8093}, "type"=>1}, {"address"=>"Korteraarseweg 48, 2461 GM Ter Aar, Netherlands", "category"=>"Address", "location"=>{"lat"=>52.1738, "lng"=>4.7296}, "type"=>2}], "ageGroups"=>[{"minAge"=>0, "maxAge"=>100, "count"=>1}], "passengers"=>[{"name"=>"Testing Oy", "email"=>"bookings@cabforce.com", "phone"=>"123456789"}], "notes"=>"Test Test", "product"=>{"type"=>2, "category"=>2, "pax"=>4, "bags"=>4, "properties"=>[{"type"=>1, "numberValue"=>1}, {"type"=>2, "numberValue"=>1}, {"type"=>8, "numberValue"=>1}, {"type"=>9, "numberValue"=>1}, {"type"=>12, "numberValue"=>1}, {"type"=>20, "numberValue"=>1}, {"type"=>50, "numberValue"=>1}, {"type"=>51, "numberValue"=>1}, {"type"=>56, "numberValue"=>1}, {"type"=>21, "numberValue"=>1}], "makeModel"=>"Toyota Prius or similar"}, "price"=>{"supplier"=>18.876399999999997, "services"=>0, "total"=>18.876399999999997, "included"=>["parking", "gratuity"]}, "createProperty"=>{"leg"=>"1/2", "reference"=>"testing123"}}
```

Create Request  Schema Validataion = true
now creating the booking
supplierBookingRefId =59c5080269e53c3100727c26

Create Response  Schema Validataion = true

./test.sh 3 afbb086daa16fc97e0d70a221c281a96

Cancel Request  Schema Validataion = true
Cancel Response  Schema Validataion = true

./test.sh 4 afbb086daa16fc97e0d70a221c281a96

checking status of = afbb086daa16fc97e0d70a221c281a96
Status Request  Schema Validataion = true
Status Response  Schema Validataion = true

./test.sh 5

Search Request  Schema Validataion = true
received 5 offers

Search Response  Schema Validataion = true
now selecting one of the offer and creating a booking
data used for creating booking

```
{"bookingRefId"=>"3ddc37d12fdc20d9b7520199df03e75d", "shortId"=>"759408071", "supplierPricingRefId"=>"PR-27401101", "pickupDateTime"=>"2017-10-07T13:55", "addresses"=>[{"address"=>"De Hoef Westzijde 33, 1426 AT De Hoef, Netherlands", "category"=>"Address", "location"=>{"lat"=>52.2032, "lng"=>4.8093}, "type"=>1}, {"address"=>"Korteraarseweg 48, 2461 GM Ter Aar, Netherlands", "category"=>"Address", "location"=>{"lat"=>52.1738, "lng"=>4.7296}, "type"=>2}], "ageGroups"=>[{"minAge"=>0, "maxAge"=>100, "count"=>1}], "passengers"=>[{"name"=>"Testing Oy", "email"=>"bookings@cabforce.com", "phone"=>"123456789"}], "notes"=>"Test Test", "product"=>{"type"=>2, "category"=>2, "pax"=>4, "bags"=>4, "properties"=>[{"type"=>1, "numberValue"=>1}, {"type"=>2, "numberValue"=>1}, {"type"=>8, "numberValue"=>1}, {"type"=>9, "numberValue"=>1}, {"type"=>12, "numberValue"=>1}, {"type"=>20, "numberValue"=>1}, {"type"=>50, "numberValue"=>1}, {"type"=>51, "numberValue"=>1}, {"type"=>56, "numberValue"=>1}, {"type"=>21, "numberValue"=>1}], "makeModel"=>"Toyota Prius or similar"}, "price"=>{"supplier"=>18.876399999999997, "services"=>0, "total"=>18.876399999999997, "included"=>["parking", "gratuity"]}, "createProperty"=>{"leg"=>"1/2", "reference"=>"testing123"}}
```

Create Request  Schema Validataion = true
now creating the booking
supplierBookingRefId =59c5086f69e53c3100727c27

Create Response  Schema Validataion = true
checking status of = 3ddc37d12fdc20d9b7520199df03e75d

Status Request  Schema Validataion = true
Status Response  Schema Validataion = true
Cancel Request  Schema Validataion = true
Cancel Response  Schema Validataion = true
checking status of = 3ddc37d12fdc20d9b7520199df03e75d

Status Request  Schema Validataion = true
Status Response  Schema Validataion = true


#### 1 thread a second ####

./test.sh 6 1

Oostervantstraat 12B, 3021 PV Rotterdam, Netherlands - Mijnsherenlaan 16, 3081 CA Rotterdam, Netherlands
-received 5 offers in 0.361617414

(AMS) - Schiphol Airport, Amsterdam, Netherlands - Sarphatistraat 117, 1018 GB, Amsterdam, Netherlands
-received 5 offers in 0.395711203

(RTM) - Rotterdam The Hague Airport, Rotterdam, Netherlands - Mijnsherenlaan 47A, 3081 GC Rotterdam, Netherlands
-received 5 offers in 0.410642091

(AMS) - Schiphol Airport, Amsterdam, Netherlands - Arlandaweg 10-14, 1043 EW, Amsterdam, Netherlands
-received 5 offers in 0.400326548 

Molenwerf 1, 1014 AG, Amsterdam, Netherlands - (AMS) - Schiphol Airport, Amsterdam, Netherlands
-received 5 offers in 0.373700579


#### 2 threads a second ####

./test.sh 6 2

Oostervantstraat 12B, 3021 PV Rotterdam, Netherlands - Mijnsherenlaan 16, 3081 CA Rotterdam, Netherlands
-received 5 offers in 0.362296315
-received 5 offers in 0.363247723

(AMS) - Schiphol Airport, Amsterdam, Netherlands - Sarphatistraat 117, 1018 GB, Amsterdam, Netherlands
-received 5 offers in 0.314868998
-received 5 offers in 0.471460523

(RTM) - Rotterdam The Hague Airport, Rotterdam, Netherlands - Mijnsherenlaan 47A, 3081 GC Rotterdam, Netherlands
-received 5 offers in 0.365419245
-received 5 offers in 0.371034361

(AMS) - Schiphol Airport, Amsterdam, Netherlands - Arlandaweg 10-14, 1043 EW, Amsterdam, Netherlands
-received 5 offers in 0.29483922
-received 5 offers in 0.298529772

Molenwerf 1, 1014 AG, Amsterdam, Netherlands - (AMS) - Schiphol Airport, Amsterdam, Netherlands
-received 5 offers in 0.282042034
-received 5 offers in 0.411367534

#### 5 threads a second ####

./test.sh 6 5

Oostervantstraat 12B, 3021 PV Rotterdam, Netherlands - Mijnsherenlaan 16, 3081 CA Rotterdam, Netherlands
-received 5 offers in 0.32365231
-received 5 offers in 0.404767611
-received 5 offers in 0.405037531
-received 5 offers in 0.4062037
-received 5 offers in 0.409807868

(AMS) - Schiphol Airport, Amsterdam, Netherlands - Sarphatistraat 117, 1018 GB, Amsterdam, Netherlands
-received 5 offers in 0.332960162
-received 5 offers in 0.334156399
-received 5 offers in 0.424172861
-received 5 offers in 0.451100869
-received 5 offers in 0.45244478 

(RTM) - Rotterdam The Hague Airport, Rotterdam, Netherlands - Mijnsherenlaan 47A, 3081 GC Rotterdam, Netherlands
-received 5 offers in 0.311756592
-received 5 offers in 0.331229156
-received 5 offers in 0.331037441
-received 5 offers in 0.428533746
-received 5 offers in 0.430136899 

(AMS) - Schiphol Airport, Amsterdam, Netherlands - Arlandaweg 10-14, 1043 EW, Amsterdam, Netherlands
-received 5 offers in 0.38152261
-received 5 offers in 0.403841785
-received 5 offers in 0.406370947
-received 5 offers in 0.408279462
-received 5 offers in 0.41076179 

Molenwerf 1, 1014 AG, Amsterdam, Netherlands - (AMS) - Schiphol Airport, Amsterdam, Netherlands
-received 5 offers in 0.344451569
-received 5 offers in 0.345617898
-received 5 offers in 0.348137098
-received 5 offers in 0.348203549
-received 5 offers in 0.418301242 

