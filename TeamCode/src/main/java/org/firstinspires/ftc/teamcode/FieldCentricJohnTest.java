package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="FieldCentricJohnTest", group="DriveModes")
public class FieldCentricJohnTest extends LinearOpMode {
    private CRServo extension;

    // Not needed but keep for future
    double leftClawPosition;
    double rightClawPosition;
    double leftClawMovedPosition = 0;
    double rightClawMovedPosition = 0;
    boolean switchDirectionLeft = false;
    boolean switchDirectionRight = false;

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
        CRServoImplEx extension = hardwareMap.get(CRServoImplEx.class, "extension");
        // Claw Motors
        Servo rightClaw = hardwareMap.servo.get("rightClaw");
        Servo leftClaw = hardwareMap.servo.get("leftClaw");

        FR.setDirection(DcMotor.Direction.REVERSE);
        BR.setDirection(DcMotor.Direction.REVERSE);

        // Slider presets
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setTargetPosition(0);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int slideTarget = 0;
        waitForStart();
        slide.setPower(2.9);

        //IMU
        // Retrieve the IMU from the hardware map
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        // Technically this is the default, however specifying it is clearer
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        // Without this, data retrieving from the IMU throws an exception
        imu.initialize(parameters);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            double y = gamepad1.left_stick_y;
            double x = -gamepad1.left_stick_x;
            double rx = -gamepad1.right_stick_x;
            //double rY = -gamepad1.right_stick_y;

            // Read inverse IMU heading, as the IMU heading is CW positive
            double botHeading = -imu.getAngularOrientation().firstAngle;

            double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
            double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

            // Gamepad Colors lmao

            Gamepad.LedEffect rainbowManual = new Gamepad.LedEffect.Builder()
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
            gamepad1.setLedColor(102, 0, 204, 100000);
            // pink
            gamepad2.setLedColor(255, 51, 255, 100000);

            // rainbow

            //gamepad1.runLedEffect(rainbowManual);
            //gamepad2.runLedEffect(rainbowManual);

            /*
            Slide motor movement
                [button controls:]
                Y makes slider go to level 3 pole
                B makes slider go to level 2 pole
                X makes slider go to level 1 pole
                A makes slider go all the way down
            */
            if (gamepad2.a) {
                slideTarget = 0;
            } else if (gamepad2.y) {
                slideTarget = 4400;
            } else if (gamepad2.b) {
                slideTarget = 3000;
            } else if (gamepad2.x) {
                slideTarget = 1350;
            }
            slide.setTargetPosition(slideTarget);

            //speed servo movement
            
            
            if (gamepad2.dpad_up) {
                extension.setPower(-1);
            } else if (gamepad2.dpad_down) {
                extension.setPower(1);
            } else {
                extension.setPower(0);
            }

                //CR motor uses setPower()
                //servo uses setPostion();



            if (gamepad2.left_bumper) {
                leftClaw.setPosition(0.1);
                rightClaw.setPosition(1);
            }

            //close claw servo movement
            else if (gamepad2.right_bumper) {
                leftClaw.setPosition(0.4);
                rightClaw.setPosition(0.8);
            }
            else {
                leftClaw.setPosition(0.1);
                rightClaw.setPosition(1);
            }


            double mult = 1;

            if(gamepad1.right_bumper) {
                mult = 0.5;
            }



            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            // the 1 * is for correcting drift btw

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = 0.85 * (mult * (rotY + rotX + rx)) / denominator;
            double backLeftPower = 1 * (mult * (rotY - rotX + rx)) / denominator;
            double frontRightPower = 1 * (mult * (rotY - rotX - rx)) / denominator;
            double backRightPower = 1 * (mult * (rotY + rotX - rx)) / denominator;

            FL.setPower(frontLeftPower);
            BL.setPower(backLeftPower);
            FR.setPower(frontRightPower);
            BR.setPower(backRightPower);


            if (gamepad1.dpad_up) {
                imu.initialize(parameters);
            }

            telemetry.addData("Slide", slide.getCurrentPosition());
            System.out.println(slide.getCurrentPosition());
        }
    }
}
