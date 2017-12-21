# WatsonInnovation
Repository for the Watson Innovation course project

## Requirements
In order to use this code, the following requirements must be satisfied:

1) `Java JDK 7` or higher
2) `Python 2.7` and `Python 3`
3) `os, argparse, time, sys, urllib2, and json` Python modules (installable via pip)

## How to use
Currently, Pearl does not have an user interface. Consequently, it is possible to explore its features with `.jar` file with command lines options.

Firstly, you need to download all the files from the following link: []

Secondly, you need to open the `config.csv` file and insert there the credentials (separated from their identifier by one `tab`) for the following services:

- Watson Visual Recognition
- Watson Natural Language Understanding
- Watson Discovery (and Environment & Collection identifiers)

Finally, you need to edit the `europeana-client.properties` file and add your API key for Europeana.

Now you are ready to use the jar file. You can start the program launching the command `java -jar Backend.jar`.
Once it is running, the console will ask you if you want to perform a training of the models (option `t`) or a benchmarking of your models (option `b`). Obviously, to perform a benchmark it is necessary to train the model first - which might take up to one hour.

During the training process, the application will save all the information about paintings in the `res/smartdb.csv` file and all the images downloaded from Google in the `images/` directory. Finally, in the `json/` folder there will be all the informations related to the painings saved in `json` format, ready to be processed by Watson Discovery (reminder: due to API limitations, the files need to be uploaded manually).

If you want to perform a benchmark of Discovery, you can do it by checking how the service performs answering the queries contained in the `res/queries.csv` file, checking the answer provided in the folder `queriesResults`.
On the other hand, if you want to benchmark the Visual Recognition service, you can choose via console the subfolder contained in `res/benchmark/` that holds the images to classify.



*Have fun!*

