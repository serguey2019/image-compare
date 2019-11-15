# The test task description
Please create a Spring Boot application which has the following RESTful API endpoints:

1) Create an endpoint which accepts a String parameter. The String parameter represents a JSON array. Convert all the values of each object in the JSON array to a MD5 hash of that value. Return the transformed JSON array in the response.

2) Create an endpoint which accepts two String parameters. Each String represents a path to an image stored in an S3 bucket. Analyze whether they're identical image files. Return a Boolean of the result in the response.

3) Create an endpoint which accepts a List of Strings. The List of Strings represents the paths to n number of images stored in an S3 bucket. Analyze which image files are identical in the List. Return a hashmap which has the image paths grouped by identical sets of images.

4) Optional Bonus Challenge - Create an endpoint which accepts a List of Strings. The List of Strings represents the paths to n number of images stored in an S3 bucket. Analyze which images are the same type of content. Assume that the images do not have any metadata associated to them. Return a hashmap where the key is a tag that describes the content of the image, and the value is a List of images of the same content. For instance if there are two different images of the Moon, return the hashmap: {"moon":["path_to_image_of_moon1","path_to_image_of_moon2"]} You may stub out the function which determines if the images are of the same thing, but in a comment you must describe in detail what you would use and do to determine if they are the same.

# Run application

## Build

```shell script
./mvnw clean package
```

## Test

```shell script
./mvnw test
```

## Start
```shell script
java -jar target/image-compare-0.0.1-SNAPSHOT.jar
```

## Call APIs

### Test case 1

```shell script
curl -X POST \
  http://localhost:8088/hash \
  -H 'content-type: application/json' \
  -d '[
   {
      "the":"json",
      "with":{
         "a":"lot",
         "of":{
            "deep":"objects"
         }
      }
   },
   {
      "another":"json"
   },
   {
      "one":"more",
      "json":{
         "to":{
            "test":{
               "deep":{
                  "fields":"processing"
               }
            }
         }
      }
   },
   "singleValue",
   [
      {
         "and":"deep",
         "object":{
            "inside":"array"
         }
      }
   ]
]'
```

### Test case 2

```shell script
curl -X POST \
  http://localhost:8088/compare \
  -H 'content-type: application/json' \
  -d '{
	"firstImagePath": "/path/to/first/image",
	"secondImagePath": "path/to/second/image"
}'
```

### Test case 3

```shell script
curl -X POST \
  http://localhost:8088/group \
  -H 'content-type: application/json' \
  -d '[
	"/path/to/images/gray_moon.png",
	"/path/to/images/white_moon.png",
	"/path/to/images/red_moon.png",
	"/path/to/images/bird.png",
	"/path/to/images/diagram.png",
	"/path/to/images/diagram1.png",
	"/path/to/images/diagram2.png"
]'
```

### Test case 4

```shell script
curl -X POST \
  http://localhost:8088/groupByName \
  -H 'content-type: application/json' \
  -d '[
	"/path/to/images/gray_moon.png",
	"/path/to/images/white_moon.png",
	"/path/to/images/red_moon.png",
	"/path/to/images/bird.png",
	"/path/to/images/diagram.png",
	"/path/to/images/diagram1.png",
	"/path/to/images/diagram2.png"
]'
```

## Services

#### CompareImagesService

Main logic of compare groups of images

#### DeepHashService

Calculate hash for values recursively

#### S3FileService

Work with s3 to provide operations like
* get file name
* download file (mocked)

#### NaturalLanguageProcessingService

Contains logic to determinate images group name based on file names. Algorithm:
* Split file name by non alphabetical symbols to get set of words describing the file
* Stemming words to get general form of words
* Calculate word usage in file names
* Get most frequently used word

#### ImagesAnalyserService

Compare two images and return true if they are identical. Service is mocked, but I can suggest the following algorithm for images comparision:
* Prepare two matrices A and B for images. Both matrices has same width, height 
* Determinate size of matrices: take less of width of A,B and take less height of A,B
* Convert original image from RGB color model to the HSV model.
* Fill the A and B matrices using Saturation component from HSV model
    * Determinate two processing windows winA/winB for A and B images. Take weight and height for windows equals 3X3 pixel (Actually we can manage windows size, but will use this one for example). Limitation:
        * Window height should be: A.height * winA.height > originImageHeight
        * Window width should be: A.width * winA.width > originImageWidth
    * For each cell in A do:
        * Calculate position in in original image for window center cell
        * Get S component from original image and fill in window
        * Calculate avg value for the window values
        * Fill A cell with average value
* Calculate matrix C (same width, height like for A,B matrices). For each cell:
    * Calculate A[i, j].value - B[i, j].value
* Calculate avg for all cells in C
* Compare avg value with the threshold value (should be configurable). If avg less than threshold then return true, otherwise false