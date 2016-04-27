TOUR OF DATA MINING ALGORITHMS README

See User Manual for more detail.  This is to get you started quickly.
You need Java 8. jar/test files in same dir.


Apriori: java -jar dm-proj.jar -a apriori -i apriori_simple-test.txt --min-sup 2 -d comma
Apriori: java -jar dm-proj.jar -a apriori -i apriori_belgium-retail-market.dat --min-sup 1500
ID3: java -jar dm-proj.jar -a id3 -i id3_simple-training.txt --label-index 4 --testing-file id3_simple-testing.txt
ID3: java -jar dm-proj.jar -a id3 -i id3_simple-training-2.txt --label-index 4 --testing-file id3_simple-testing-2.txt
XMeans: java -jar dm-proj.jar -a xmeans -i xmeansMidTest --min-k 1 --max-k 5
XMeans verbose: java -jar dm-proj.jar -a xmeans -i xmeansMidTest --min-k 1 --max-k 5 -v