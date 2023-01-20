package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.*;

@Autonomous(name="parent Auton Don't Run")
public class parentAuton extends LinearOpMode {

    public static double right = 300, left = -300;

    // adjust these to change the multiplier for how fast the wheels are individually
    double FLMulti = 0.5;
    double BLMulti = 0.5;
    double FRMulti = 0.5;
    double BRMulti = 0.5;

    // adjust these to adjust how far the individual motors go in ticks * ticks MUST be in INT!!
    // ex. if you make it 100, motor will go +100 ticks. if you make it -100, it will go -100 ticks
    int FRtickAdj = 0;
    int FLtickAdj = 0;
    int BRtickAdj = 0;
    int BLtickAdj = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while (opModeIsActive()) {}
    }

    // Note on how the constructors in this class work:
    // all motors and servos are declared in ParkAuton, and are thrown through
    // parameters in the methods below

        /*
        Ngl i dont know how this works- if the robot is in position 500, and I tell it to go to 200,
        do I need to make the velocity negative? does negative velocity even do anything? man tf is this
        this hurts my brain


        remember - ticks are what the motors 'count' by.

         */

    public void forwardInTicks(double mult, int ticks, DcMotorEx myFrontRight,
                             DcMotorEx myFrontLeft, DcMotorEx myBackRight, DcMotorEx myBackLeft) {

        /*
            TO USE ENCODERS!
            order MUST go: reset encoder --> set position --> run to position --> set max velocity
            it will compile, but when you try to run it will get mad and yell at you
         */

            myFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            myFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            myBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            myBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // remember - ticks are what the motors 'count' by.
            myFrontRight.setTargetPosition(ticks + FRtickAdj);
            myFrontLeft.setTargetPosition(ticks + FLtickAdj);
            myBackRight.setTargetPosition(ticks + BRtickAdj);
            myBackLeft.setTargetPosition(ticks + BLtickAdj);

            myFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            myFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            myBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            myBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // i have no idea what the no idea why 0.75 & 0.9 exist, but if it works, dont touch it
            myFrontRight.setVelocity(mult * right * FRMulti);
            myFrontLeft.setVelocity(mult * left * 0.75 * FLMulti);
            myBackRight.setVelocity(mult * right * BRMulti);
            myBackLeft.setVelocity(mult * left * 0.9 * BLMulti);

    }

    public void backwardsInTicks(double mult, int ticks, DcMotorEx myFrontRight,
                               DcMotorEx myFrontLeft, DcMotorEx myBackRight, DcMotorEx myBackLeft) {


        myFrontRight.setTargetPosition(ticks + FRtickAdj);
        myFrontLeft.setTargetPosition(ticks + FLtickAdj);
        myBackRight.setTargetPosition(ticks + BRtickAdj);
        myBackLeft.setTargetPosition(ticks + BLtickAdj);

        myFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        myFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        myBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        myBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        myFrontRight.setVelocity(mult * right * FRMulti);
        myFrontLeft.setVelocity(mult * left * FLMulti);
        myBackRight.setVelocity(mult * right * BRMulti);
        myBackLeft.setVelocity(mult * left * BLMulti);

    }

    // im gonna be real i have no idea if this works, but it should, in theory
    public void waitABit(long ms, DcMotorEx myFrontRight,
                         DcMotorEx myFrontLeft, DcMotorEx myBackRight, DcMotorEx myBackLeft) {

        ElapsedTime runTime = new ElapsedTime();
        double startTime = runTime.milliseconds();
        double currentTime = runTime.milliseconds();

        while (currentTime - startTime <= ms){

          myFrontRight.setVelocity(0);
          myFrontLeft.setVelocity(0);
          myBackRight.setVelocity(0);
          myBackLeft.setVelocity(0);
          currentTime = runTime.milliseconds();
        }
    }

    public void driveForward(double mult, long ms, DcMotorEx myFrontRight,
                             DcMotorEx myFrontLeft, DcMotorEx myBackRight, DcMotorEx myBackLeft) {
        ElapsedTime runTime = new ElapsedTime();double startTime = runTime.milliseconds();
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
        //sleep(2000);
    }

    public void openClaw(Servo leftServo, Servo rightServo) {
        leftServo.setPosition(0.3);
        rightServo.setPosition(0.7);
        //sleep(2000);
    }
}
