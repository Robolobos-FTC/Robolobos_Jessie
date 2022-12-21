package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="DaniDrive", group="DriveModes")
public class DaniDrive extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Wheel Motors
        DcMotor FL = hardwareMap.dcMotor.get("front left");
        DcMotor BL = hardwareMap.dcMotor.get("back left");
        DcMotor FR = hardwareMap.dcMotor.get("front right");
        DcMotor BR = hardwareMap.dcMotor.get("back right");
        // Slider
        DcMotor slide = hardwareMap.dcMotor.get("slide");
        // Claw extension
        CRServo extension = hardwareMap.crservo.get("extension");
        // Claw Motors
        CRServo rightClaw = hardwareMap.crservo.get("rightClaw");
        CRServo leftClaw = hardwareMap.crservo.get("leftClaw");
        // Wheel presets
        FR.setDirection(DcMotor.Direction.REVERSE);
        BR.setDirection(DcMotor.Direction.REVERSE);
        FL.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.REVERSE);
        // Slider presets
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setTargetPosition(0);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int slideTarget = 0;
        waitForStart();
        slide.setPower(1);

        // Claw presets
        rightClaw.setPower(0);
        leftClaw.setPower(0);

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;
            //double rY = -gamepad1.right_stick_y;

            //Slide motor movement
            if (gamepad2.a) slideTarget = 0;
            else if (gamepad2.y) slideTarget = -2300;
            slide.setTargetPosition(slideTarget);

            //Slide servo movement
            if (gamepad2.dpad_up) extension.setPower(1);
            else if (gamepad2.dpad_down) extension.setPower(-1);
            else extension.setPower(0);

            //open claw servo movement
            if (gamepad2.b) {
                leftClaw.setPower(-.5);
                rightClaw.setPower(.5);
            }
            //close claw servo movement
            else if (gamepad2.x) {
                leftClaw.setPower(.5);
                rightClaw.setPower(-.5);
            }
            else if(leftClaw.getPower() == -0.5){
                leftClaw.setPower(0);
                rightClaw.setPower(0);
            }

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = -(y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            FL.setPower(frontLeftPower);
            BL.setPower(backLeftPower);
            FR.setPower(frontRightPower);
            BR.setPower(backRightPower);

            telemetry.addData("Slide", slide.getCurrentPosition());
            System.out.println(slide.getCurrentPosition());
        }
    }
}
