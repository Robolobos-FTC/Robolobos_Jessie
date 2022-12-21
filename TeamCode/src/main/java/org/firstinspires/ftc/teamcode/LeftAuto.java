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

@Autonomous(name="Left Auto")

public class LeftAuto extends LinearOpMode {
    public DcMotorEx frontRight, frontLeft, backRight, backLeft, slide;
    public CRServo extension;
    public Servo rightClaw, leftClaw;

    private double right = 300, left = -300;

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
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        slide.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        slide.setTargetPosition(0);
        slide.setPower(1);

        parentAuton bot = new parentAuton();
        waitForStart();
        while (opModeIsActive()) {
            bot.driveForward(5.0, 1900, frontRight, frontLeft, backRight, backLeft); /*
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
           */
            stop();
        }
    }
}