package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.*;

@Autonomous(name="parent Auton Don't Run")
public class parentAuton extends LinearOpMode {
    public static double right = 300, left = -300;
    double FLMulti = 55;
    double BLMulti = .5;
    double FRMulti = 0.425; // 0.925 for FR and BR
    double BRMulti = 0.425;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while (opModeIsActive()) {}
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

    public void moveSlide(int slideTarget, DcMotorEx mySlide) {
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
