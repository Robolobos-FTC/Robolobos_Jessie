package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.zip.DataFormatException;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.*;

@Autonomous(name="parent Auton Don't Run")

public class parentAuton extends LinearOpMode {
    public static double right = 300, left = -300;
    public static double mult = 1.0;
    double FLMulti = 1;
    double BLMulti = 1;
    double FRMulti = 0.925;
    double BRMulti = 0.925;

    @Override
    public void runOpMode() throws InterruptedException {
        //runtime.reset();


        // Retrieve the IMU from the hardware map
        //BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        //BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        // Technically this is the default, however specifying it is clearer
        //parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;

        // Without this, data retrieving from the IMU throws an exception
        //imu.initialize(parameters);

        waitForStart();

        //if (isStopRequested()) return;

        while (opModeIsActive()) {

            //double y = gamepad1.left_stick_y;
            //double x = -gamepad1.left_stick_x;
            //double rx = -gamepad1.right_stick_x;
            //double rY = -gamepad1.right_stick_y;

            // Read inverse IMU heading, as the IMU heading is CW positive
            //double botHeading = -imu.getAngularOrientation().firstAngle;

            //double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
            //double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);
            /*
                Field-Centric wheel motors below with
            */

        }
    }


    public void driveForward(double mult, long ms, DcMotorEx myFrontRight,
                             DcMotorEx myFrontLeft, DcMotorEx myBackRight, DcMotorEx myBackLeft) {
        ElapsedTime runTime = new ElapsedTime();
        double startTime = runTime.milliseconds();
        double currentTime = runTime.milliseconds();
        while (currentTime - startTime <= ms) {
            myFrontRight.setVelocity(mult * right * FRMulti);
            myFrontLeft.setVelocity(mult * left * 0.75 * FLMulti);
            myBackRight.setVelocity(mult * right * BRMulti);
            myBackLeft.setVelocity(mult * left * 0.9 * BLMulti);
            currentTime = runTime.milliseconds();
        }

        myFrontRight.setVelocity(0);
        myFrontLeft.setVelocity(0);
        myBackRight.setVelocity(0);
        myBackLeft.setVelocity(0);
    }

    public void driveBackward(double mult, long ms, DcMotorEx myFrontRight,
                              DcMotorEx myFrontLeft, DcMotorEx myBackRight, DcMotorEx myBackLeft) {
        ElapsedTime runTime = new ElapsedTime();
        double startTime = runTime.milliseconds();
        double currentTime = runTime.milliseconds();
        while (currentTime - startTime <= ms) {
            myFrontRight.setVelocity(-mult * right * FRMulti);
            myFrontLeft.setVelocity(-mult * left * FLMulti);
            myBackRight.setVelocity(-mult * right * BRMulti);
            myBackLeft.setVelocity(-mult * left * BLMulti);
            currentTime = runTime.milliseconds();
        }

        myFrontRight.setVelocity(0);
        myFrontLeft.setVelocity(0);
        myBackRight.setVelocity(0);
        myBackLeft.setVelocity(0);
    }

    public void rotateRight(double mult, int ms, DcMotorEx myFrontRight,
                            DcMotorEx myFrontLeft, DcMotorEx myBackRight, DcMotorEx myBackLeft) {
        ElapsedTime runTime = new ElapsedTime();
        double startTime = runTime.milliseconds();
        double currentTime = runTime.milliseconds();
        while (currentTime - startTime <= ms) {
            myFrontRight.setVelocity(mult * -right * FRMulti);
            myFrontLeft.setVelocity(mult * left * FLMulti);
            myBackRight.setVelocity(mult * -right * BRMulti);
            myBackLeft.setVelocity(mult * left * BLMulti);
            currentTime = runTime.milliseconds();
        }

        myFrontRight.setVelocity(0);
        myFrontLeft.setVelocity(0);
        myBackRight.setVelocity(0);
        myBackLeft.setVelocity(0);
    }

    public void rotateLeft(double mult, int ms, DcMotorEx myFrontRight,
                           DcMotorEx myFrontLeft, DcMotorEx myBackRight, DcMotorEx myBackLeft) {
        ElapsedTime runTime = new ElapsedTime();
        double startTime = runTime.milliseconds();
        double currentTime = runTime.milliseconds();
        while (currentTime - startTime <= ms) {
            myFrontRight.setVelocity(mult * right * FRMulti);
            myFrontLeft.setVelocity(mult * -left * FLMulti);
            myBackRight.setVelocity(mult * right * BRMulti);
            myBackLeft.setVelocity(mult * -left * BLMulti);
            currentTime = runTime.milliseconds();
        }

        myFrontRight.setVelocity(0);
        myFrontLeft.setVelocity(0);
        myBackRight.setVelocity(0);
        myBackLeft.setVelocity(0);
    }

    public void strafeRight(double mult, int ms, DcMotorEx myFrontRight,
                            DcMotorEx myFrontLeft, DcMotorEx myBackRight, DcMotorEx myBackLeft) {
        ElapsedTime runTime = new ElapsedTime();
        double startTime = runTime.milliseconds();
        double currentTime = runTime.milliseconds();
        while (currentTime - startTime <= ms) {
            myFrontRight.setVelocity(mult * -right * FRMulti);
            myFrontLeft.setVelocity(mult * left * FLMulti);
            myBackRight.setVelocity(mult * right * BRMulti);
            myBackLeft.setVelocity(mult * -left * BLMulti);
            currentTime = runTime.milliseconds();
        }

        myFrontRight.setVelocity(0);
        myFrontLeft.setVelocity(0);
        myBackRight.setVelocity(0);
        myBackLeft.setVelocity(0);
    }

    public void strafeLeft(double mult, int ms, DcMotorEx myFrontRight,
                           DcMotorEx myFrontLeft, DcMotorEx myBackRight, DcMotorEx myBackLeft) {
        ElapsedTime runTime = new ElapsedTime();
        double startTime = runTime.milliseconds();
        double currentTime = runTime.milliseconds();
        while (currentTime - startTime <= ms) {
            myFrontRight.setVelocity(mult * right * FRMulti);
            myFrontLeft.setVelocity(mult * -left * FLMulti);
            myBackRight.setVelocity(mult * -right * BRMulti);
            myBackLeft.setVelocity(mult * left * BLMulti);
            currentTime = runTime.milliseconds();
        }

        myFrontRight.setVelocity(0);
        myFrontLeft.setVelocity(0);
        myBackRight.setVelocity(0);
        myBackLeft.setVelocity(0);
    }

    public void raiseSlide(int slideTarget, DcMotorEx mySlide) {
        while (mySlide.getCurrentPosition() != slideTarget) {
            mySlide.setTargetPosition(slideTarget);
        }
    }

    public void raiseExtension(int ms, CRServo myExtension) {
        // calibrate it to reach the max height
        ElapsedTime runTime = new ElapsedTime();
        double startTime = runTime.milliseconds();
        double currentTime = runTime.milliseconds();
        while (currentTime - startTime <= ms) {
            myExtension.setPower(1);
            currentTime = runTime.milliseconds();
        }
        myExtension.setPower(0);
    }

    public void closeClaw(Servo leftServo, Servo rightServo) {
        leftServo.setPosition(0.1);
        rightServo.setPosition(1);
        sleep(2000);
    }

    public void openClaw(Servo leftServo, Servo rightServo) {
        leftServo.setPosition(0.3);
        rightServo.setPosition(0.7);
        sleep(2000);
    }
}