/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Autonomous
public class ParkAuton extends LinearOpMode {
    public DcMotorEx frontRight, frontLeft, backRight, backLeft, mySlide;
    public CRServo extension;
    public Servo rightClaw, leftClaw;
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

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

        // Motors
        frontRight = hardwareMap.get(DcMotorEx.class, "front right");
        frontLeft = hardwareMap.get(DcMotorEx.class, "front left");
        backRight = hardwareMap.get(DcMotorEx.class, "back right");
        backLeft = hardwareMap.get(DcMotorEx.class, "back left");

        // Slider
        mySlide = hardwareMap.get(DcMotorEx.class, "slide");

        // extension + claws
        extension = hardwareMap.crservo.get("extension");
        rightClaw = hardwareMap.servo.get("rightClaw");
        leftClaw = hardwareMap.servo.get("leftClaw");

        // sets all encoder values to 0- not necessary as it is default, but here its visualized
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mySlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // this tells motors to count 'ticks' while 'running to [set] position'
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mySlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Slide * this is just declaring
        mySlide.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        mySlide.setTargetPosition(0);
        mySlide.setPower(1);



        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(640,360, OpenCvCameraRotation.SIDEWAYS_RIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });

        telemetry.setMsTransmissionInterval(50);

        while (!isStarted() && !isStopRequested()) {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();
            if (currentDetections.size() != 0) {
                boolean tagFound = false;
                for(AprilTagDetection tag : currentDetections) {
                    if(tag.id == left || tag.id == middle || tag.id == right) {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound) {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                } else {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null) {
                        telemetry.addLine("(The tag has never been seen)");
                    } else {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }
            } else {
                telemetry.addLine("Don't see tag of interest :(");

                if (tagOfInterest == null) {
                    telemetry.addLine("(The tag has never been seen)");
                } else {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }
            }
            telemetry.update();
            sleep(20);
        }

        /* Update the telemetry */
        if(tagOfInterest != null) {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        } else {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :( L ");
            telemetry.update();
        }

        while (opModeIsActive()) {
            // Gamepad Colors lmao

            Gamepad.LedEffect rainbowAuton = new Gamepad.LedEffect.Builder()
                    .addStep(1, 0, 0, 5000) // Show red for 5s
                    .addStep(255, 128, 0, 5000) // Show orange for 5s
                    .addStep(255, 255, 51, 5000) // Show yellow for 5s
                    .addStep(0, 1, 0, 5000) // Show green for 5s
                    .addStep(0, 0, 1, 5000) // Show blue for 5s
                    .addStep(102, 0, 204, 5000) // Show purple for 5s
                    .addStep(1, 1, 1, 5000) // Show white for 5s
                    .addStep(255, 51, 255, 50000) // Show pink for 50s

                    .build();


            // purple
            //gamepad1.setLedColor(102, 0, 204, 10000);
            // pink
            //gamepad2.setLedColor(255, 51, 255, 10000);

            // rainbow

                gamepad1.runLedEffect(rainbowAuton);
                gamepad2.runLedEffect(rainbowAuton);


/*
To-Code:
add a 'ticks' method! to PARENT AUTON! it will be more annoying to try and add it below

ex. bot.driveForwardTicks( [ticks:] 800, [velocity:] 3, [wheel motors]);

once a 'tickRPM' method is added, RUN PHYSICAL TESTS TO MEASURE HOW MANY TICKS = ROUGHLY 1 TILE!!!!


get code to always stay centered using IMU Output

maybe try for a PID controller use?
and if we somehow manage to do all of the above (mad unlikely), work on color sensors & proximity sensors
 */

            parentAuton bot = new parentAuton();

            // for level 2 pole
            bot.closeClaw(leftClaw, rightClaw);
            bot.driveForward(5, 150, frontRight, frontLeft, backRight, backLeft);
            bot.strafeRight(5, 1780, frontRight, frontLeft, backRight, backLeft);
            bot.rotateLeft(5, 1150, frontRight, frontLeft, backRight, backLeft);
            bot.moveSlide(2500, mySlide);
            bot.driveForward(5, 550, frontRight, frontLeft, backRight, backLeft);
            bot.openClaw(leftClaw, rightClaw);

            // Always set to 0 after finishing program so slider isnt hangin around !!!
            bot.moveSlide(0, mySlide);


            /*
            // for level 3 pole
            bot.moveSlide(3500, mySlide);
            bot.rotateLeft(5, 1600, frontRight, frontLeft, backRight, backLeft);
            bot.driveForward(5, 300, frontRight, frontLeft, backRight, backLeft);
            bot.openClaw(leftClaw, rightClaw);
            bot.moveSlide(0, mySlide);
            */







            // ** With 3rd square in mind, claw facing right

            /*
            if (tagOfInterest.id == left) {
                // left square
                bot.strafeRight(5, 750, frontRight, frontLeft, backRight, backLeft);
                bot.driveBackward(5, 500, frontRight, frontLeft, backRight, backLeft);


            } else if (tagOfInterest.id == right) {
                // right square
                bot.strafeRight(5, 750, frontRight, frontLeft, backRight, backLeft);
                bot.driveForward(5, 500, frontRight, frontLeft, backRight, backLeft);


            } else {
                // forward square
                bot.strafeRight(5, 750, frontRight, frontLeft, backRight, backLeft);
            }
        */

            stop();
        }
    }

    void tagToTelemetry(AprilTagDetection detection) {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}