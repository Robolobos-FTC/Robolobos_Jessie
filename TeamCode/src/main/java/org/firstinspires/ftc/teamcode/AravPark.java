package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
// Gamepad import is for PS4 controllers
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.ArrayList;


// Made by Arav Neroth, 1/7/2023
// Cedar Park High School Team 14363 Jessie
@Autonomous
public class AravPark extends LinearOpMode {

    /*
           This is a copy of ParkAuton that is able to park comp-ready, without cone scoring
           just in case that I don't finish the full auton by the Scrim/Comp (1/10 & 1/14)

           ** Quick Notes!
           All code below until (opMode is active) will start on 'Initialization'
           (initialization is when the start button is pressed once, and when the
           camera will be searching for tags, so don't instantly double tap the start
           button, otherwise the camera can't recognize tags )
     */


    // Motors + Camera + Servo Declaration
    public DcMotorEx frontRight, frontLeft, backRight, backLeft, mySlide;
    public CRServo extension;
    public Servo rightClaw, leftClaw;
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    // take a guess what this does
    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    // Tag Ids for sleeve
    int left = 3;
    int middle = 4;
    int right = 6;

    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode() {

        // Fetching Data from FTC DriverStation App (hardwareMap)

        // Wheels
        frontRight = hardwareMap.get(DcMotorEx.class, "front right");
        frontLeft = hardwareMap.get(DcMotorEx.class, "front left");
        backRight = hardwareMap.get(DcMotorEx.class, "back right");
        backLeft = hardwareMap.get(DcMotorEx.class, "back left");

        // Slider
        mySlide = hardwareMap.get(DcMotorEx.class, "slide");

        // Extension + Claws
        extension = hardwareMap.crservo.get("extension");
        rightClaw = hardwareMap.servo.get("rightClaw");
        leftClaw = hardwareMap.servo.get("leftClaw");

        // Encoders on (Not Used in this code**)
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mySlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Slide Instructions
        mySlide.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        mySlide.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        mySlide.setTargetPosition(0);
        mySlide.setPower(1);


        // Camera Setup, its a lot but simplified its just calling from the April Pipeline + Phone
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        // this line is the actual creating of the camera, using res declared above
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // this is self explanatory- a 6th grader could understand what this does
                camera.startStreaming(640, 360, OpenCvCameraRotation.SIDEWAYS_RIGHT);
            }

            @Override
            public void onError(int errorCode) {
            }
        });

        telemetry.setMsTransmissionInterval(50);

        // this is a lot of logic ngl, but what it does is while the bot is in 'initilization' mode,
        // camera will be 'detecting' for any april tags it sees, and will update preset values if it does
        while (!isStarted() && !isStopRequested()) {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();
            if (currentDetections.size() != 0) {
                boolean tagFound = false;
                for (AprilTagDetection tag : currentDetections) {
                    if (tag.id == left || tag.id == middle || tag.id == right) {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                // this is just a bunch of text being printed for information so we
                // can tell what's going on with the camera through the app easily
                if (tagFound) {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry2(tagOfInterest);
                } else {
                    telemetry.addLine("Don't see tag of interest :(");

                    if (tagOfInterest == null) {
                        telemetry.addLine("(The tag has never been seen)");
                    } else {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry2(tagOfInterest);
                    }
                }
            } else {
                telemetry.addLine("Don't see tag of interest :(");

                if (tagOfInterest == null) {
                    telemetry.addLine("(The tag has never been seen)");
                } else {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry2(tagOfInterest);
                }
            }
            // will update the camera every 20 milliseconds
            telemetry.update();
            sleep(20);
        }

        /* Update the telemetry */
        if (tagOfInterest != null) {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry2(tagOfInterest);
            telemetry.update();
        } else {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :( L ");
            telemetry.update();
        }

        // PS4 Controller Colors for fun
        Gamepad.LedEffect primaryColors = new Gamepad.LedEffect.Builder()
                .addStep(1, 0, 0, 10000) // Show red for 10s
                .addStep(0, 1, 0, 10000) // Show green for 10s
                .addStep(0, 0, 1, 10000) // Show blue for 10s
                .addStep(1, 1, 1, 10000) // Show white for 10s
                .addStep(255, 51, 255, 50000) // Show pink for 50s

                .build();

            // controllers will run the effect above off of Initialization
             gamepad1.runLedEffect(primaryColors);
             gamepad2.runLedEffect(primaryColors);


        // opMode is basically the second time you hit the start button on the app
        // Everything below will run during the 30s period
        while (opModeIsActive()) {

            // controllers will turn purple for 30s during Auton mode
            gamepad1.setLedColor(93, 63, 211, 30000 );
            gamepad2.setLedColor(93, 63, 211, 30000 );


            // Parking Code Below

            // creates a new object 'onlyPark' from parentAuton, much like how you
            // create a new object in a 'runner' class, except think of this entire class
            // like a runner, and think of parentAuton like a class you made

            parentAuton onlyPark = new parentAuton();

            // all of this is also self-explanatory
            if (tagOfInterest.id == left) {
                onlyPark.driveBackward(5, 800, frontRight, frontLeft, backRight, backLeft);
                onlyPark.rotateRight(5, 100, frontRight, frontLeft, backRight, backLeft);
                onlyPark.strafeRight(5, 1000, frontRight, frontLeft, backRight, backLeft);

            } else if (tagOfInterest.id == right) {
                onlyPark.driveBackward(5, 900, frontRight, frontLeft, backRight, backLeft);
                sleep(200);
                onlyPark.rotateRight(5, 100, frontRight, frontLeft, backRight, backLeft);
                onlyPark.strafeLeft(5, 600, frontRight, frontLeft, backRight, backLeft);

            } else {
                onlyPark.driveBackward(5, 900, frontRight, frontLeft, backRight, backLeft);
            }

            // stop(), believe it or not, will stop the robot from expecting any other code to move
            stop();
        }

    }

    // this is just gonna print more stuff to the phone
    void tagToTelemetry2(AprilTagDetection detection) {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}