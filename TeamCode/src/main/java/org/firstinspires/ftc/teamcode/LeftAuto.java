package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Autonomous(name="Left Auto")

public class LeftAuto extends LinearOpMode {
    public DcMotorEx frontRight, frontLeft, backRight, backLeft, slide;
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
    public void runOpMode() throws InterruptedException {
        frontRight = hardwareMap.get(DcMotorEx.class, "front right");
        frontLeft = hardwareMap.get(DcMotorEx.class, "front left");
        backRight = hardwareMap.get(DcMotorEx.class, "back right");
        backLeft = hardwareMap.get(DcMotorEx.class, "back left");
        slide = hardwareMap.get(DcMotorEx.class, "slide");
        extension = hardwareMap.crservo.get("extension");
        rightClaw = hardwareMap.servo.get("rightClaw");
        leftClaw = hardwareMap.servo.get("leftClaw");

        // Chassis
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Slide
        slide.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        //slide.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        slide.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        slide.setTargetPosition(0);
        slide.setPower(1);

        parentAuton bot = new parentAuton();

        int sleeveID = getSleeveID();


        waitForStart();
        while (opModeIsActive()) {
            bot.driveForward(5.0, 1900, frontRight, frontLeft, backRight, backLeft);
            bot.rotateLeft(5.0, 450, frontRight, frontLeft, backRight, backLeft);
            bot.driveForward(5.0, 530, frontRight, frontLeft, backRight, backLeft);
            bot.closeClaw(leftClaw, rightClaw);
            bot.raiseSlide(1000, slide);
            bot.driveBackward(5.0, 500, frontRight, frontLeft, backRight, backLeft);
            bot.rotateLeft(5.0, 1100, frontRight, frontLeft, backRight, backLeft);
            bot.driveForward(5.0, 590, frontRight, frontLeft, backRight, backLeft);
            bot.raiseSlide(4199, slide);
            bot.driveForward(5.0, 150, frontRight, frontLeft, backRight, backLeft);
            bot.openClaw(leftClaw, rightClaw);
            bot.driveBackward(5.0, 500, frontRight, frontLeft, backRight, backLeft);
            bot.raiseSlide(0, slide);
            bot.rotateLeft(5.0, 850, frontRight, frontLeft, backRight, backLeft);
            bot.driveForward(5.0, 700, frontRight, frontLeft, backRight, backLeft);
            bot.closeClaw(leftClaw, rightClaw);
            //bot.strafeRight(3.0, 2000, frontRight, frontLeft, backRight, backLeft);
            //bot.strafeLeft(3.0, 2000, frontRight, frontLeft, backRight, backLeft);
            //bot.rotateRight(3.0, 2000, frontRight, frontLeft, backRight, backLeft);
            //bot.rotateLeft(3.0, 3000, frontRight, frontLeft, backRight, backLeft);
            //bot.raiseSlide(4000, 1000, slide);
            //bot.raiseExtension(3000, extension);
            //bot.raiseSlide(0, 0, slide);
            //bot.openClaw(leftClaw, rightClaw);
            //bot.closeClaw(leftClaw, rightClaw);
            //bot.raiseExtension(-2000, extension);




            // DKFLJDSLKFJlsdf
            ///fdskfljsdlkfjdslf



            // Below code to confirm camera can detect sleeve
            /*if (sleeveID == left) {
                bot.driveBackward(5, 850, frontRight, frontLeft, backRight, backLeft);
                sleep(200);
                bot.rotateRight(5, 100, frontRight, frontLeft, backRight, backLeft);
                bot.strafeRight(5, 1000, frontRight, frontLeft, backRight, backLeft);
            } else if (sleeveID == right) {
                bot.driveBackward(5, 900, frontRight, frontLeft, backRight, backLeft);
                sleep(200);
                bot.rotateRight(5, 100, frontRight, frontLeft, backRight, backLeft);
                bot.strafeLeft(5, 900, frontRight, frontLeft, backRight, backLeft);
            } else {
                bot.driveBackward(5, 900, frontRight, frontLeft, backRight, backLeft);
            }*/
            stop();
        }
    }

    public int getSleeveID() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(640, 360, OpenCvCameraRotation.SIDEWAYS_RIGHT);
            }

            @Override
            public void onError(int errorCode) {
            }
        });

        telemetry.setMsTransmissionInterval(50);

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

                if (tagFound) {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                } else {
                    telemetry.addLine("Don't see tag of interest :(");

                    if (tagOfInterest == null) {
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
        if (tagOfInterest != null) {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        } else {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :( L ");
            telemetry.update();
        }

        return tagOfInterest.id;
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