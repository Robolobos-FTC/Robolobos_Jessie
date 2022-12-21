package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "Camera Test")
public class Camera extends OpMode {
    OpenCvWebcam webcam;
    public void init() {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            public void onOpened() {
                webcam.startStreaming(640, 360, OpenCvCameraRotation.SIDEWAYS_RIGHT);
            }

            public void onError(int errorCode) {}
        });
    }

    public void loop() {

    }
}
