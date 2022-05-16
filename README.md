# MOBL
## Introduction
* Mobile Biomechanics Laboratory (MOBL) is an open-access Android-based mobile app for undergraduate biomechanics education. 
* Students can practice real-time biomechanics analysis through our app, which consists of a mobile platform-based computer-vision algorithm, a human kinematics variable estimator, and a user interface. 
<p align="center">
  <img src="https://user-images.githubusercontent.com/82116855/168656667-0586d3f3-47f7-4c13-8a63-243367374c11.PNG" width="900">
</p>

* For an insight view of MOBL, please refer to our paper <em>(Wang, H., Lu, L., Xie, Z., Su, B., & Xu, X. (2022). A mobile platform app to assist learning human kinematics in undergraduate biomechanics courses. Proceedings of the Human Factors and Ergonomics Society Annual Meeting.)</em>.<br />
## Access the code
**GITHUB use:** (When you come up with any problems, feel free to leave an issue or PR.)
  1. For windows user, click 'Code' in green button and select 'Download Zip'. Where you download it doesn't matter.
  2. Open the zip file and unzip it.
  3. The files in the zip file are exactly the same with the files shown in the Github page. The environments I build this demo are: <br />* Operation System-- Windows 10 Education; <br />* Android Studio-- 2020.3.0.0 for Windows; <br />* NDK Version-- 16.1.4479499; <br />* SDK Version-- Android 8.0 with API level 26.
  4. Finally, import the project to Android Studio and run in you smartphone.<br />
  
**Computer-vision:** <br />
* We use a Tensorflow Lite model based on convolutional pose machines (CPM). An inverted residual with a linear bottleneck module (a.k.a., MobileNet V2) is adopted for real-time inference. Instead of applying normal convolution, this method performs a sequence of depth-wise separable convolutions to immensely reduce the number of computations. <br />
* Our computer-vision method is based on the work from https://github.com/edvardHua/PoseEstimationForMobile, where we use the CV model of TFlite (Only CPU) as a start.<br />
* If you want to train your own CV model and test its performance, please refer to [edvardHua](https://github.com/edvardHua/PoseEstimationForMobile)'s work.<br />


## Download our app 
* The app includes 10 modules that can demonstrate joint angular kinematics, body segment center of mass, and jumping height. Either the back camera or the front camera can be utilized, depending on whether the app is used to capture a class partner’s motion or self-motion. 
* You are welcome to downlaod the .apk file to test on your smartphone. The download link is provided at our [MOBL](https://www.ise.ncsu.edu/biomechanics/MOBL/) site.
