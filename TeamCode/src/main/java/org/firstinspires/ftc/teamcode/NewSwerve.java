package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.*;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.*;

@TeleOp(name="New Diff", group="DriveModes")
public class NewSwerve extends OpMode {
    /*
    Max Velocity:
    145.1 Ticks per Revolution
    1150 RPM
    .setVelocity() is ticks per second
    1150/60 = 19.1 RPS
    19.1 RPS * 145.1 TPR = 2781.083 TPS
    */
    private double maxV = 1150.0/60.0*145.1 * 0.8; //max rpm 80%
    private DcMotorEx TL, BL, TR, BR;
    private double x, y, rotation, angle, TLv, TRv, BLv, BRv;
    private final int A = 2100;
    private final int B = 2100;
    private ElapsedTime runtime;
    private BNO055IMU imu;
    private Module left, right;
    private CRServo claw;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");
        runtime = new ElapsedTime();

        TL = hardwareMap.get(DcMotorEx.class, "TL"); //1
        BL = hardwareMap.get(DcMotorEx.class, "BL"); //1
        TR = hardwareMap.get(DcMotorEx.class, "TR"); //1
        BR = hardwareMap.get(DcMotorEx.class, "BR"); //1
        claw = hardwareMap.get(CRServo.class, "Claw");

        TL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        TL.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.REVERSE);
        TR.setDirection(DcMotorSimple.Direction.REVERSE);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);

        left = new Module(145.1, 106.0/13 * 18.0/14, 1);
        right = new Module(145.1, 106.0/13 * 18.0/14, -1);

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);

        telemetry.addData("Status", "Initilized");

    }
    @Override
    public void init_loop() {
    }
    @Override
    public void start() {
        runtime.reset();
    }
    @Override
    public void loop() {
        x = Math.abs(gamepad1.left_stick_x) > 0.01 ? gamepad1.left_stick_x : 0; //sideways movement
        y = Math.abs(gamepad1.left_stick_y) > 0.01 ? -gamepad1.left_stick_y : 0; //forward movement
        rotation = Math.abs(gamepad1.right_stick_x) > 0.01 ? -gamepad1.right_stick_x : 0; //rotational movement

        left.readEncoders(TL.getCurrentPosition(), BL.getCurrentPosition());
        right.readEncoders(TR.getCurrentPosition(), BR.getCurrentPosition());

        left.calc(y, x, rotation, -imu.getAngularOrientation().firstAngle);
        right.calc(y, x, rotation, -imu.getAngularOrientation().firstAngle);

        TLv = left.gearSpeed(-1, A, B);
        BLv = left.gearSpeed(1, A, B);
        TRv = right.gearSpeed(-1, A, B);
        BRv = right.gearSpeed(1, A, B);
        double ClawSpeed = gamepad1.right_trigger > 0 ? gamepad1.right_trigger : -gamepad1.left_trigger;
        claw.setPower(ClawSpeed);

        TL.setVelocity(TLv);
        BL.setVelocity(BLv);
        TR.setVelocity(TRv);
        BR.setVelocity(BRv);

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Joystick: ", /*truncate(Math.atan2(x, y) * 180 / Math.PI * 100) / 100*/y);
        telemetry.addData("RightV:   ", truncate(right.getSpeed()));
        telemetry.addData("LeftV:    ", truncate(left.getSpeed()));
        telemetry.addData("RightA:   ", truncate(right.getAngle()));
        telemetry.addData("LeftA:    ", truncate(left.getAngle()));
        telemetry.addData("Heading:  ", truncate(imu.getAngularOrientation().firstAngle));
    }
    @Override
    public void stop() {
    }
    public double truncate(double value){
        return Math.floor(value * 100) / 100.0;
    }
}

class Module {

    private double angle, rot, topV, bottomV, pivot, tick, travel, velocity, strafe, forward, speed, target;
    private int curTick, inverse, side;
    private boolean toInverse;
    private final double MOTOR_TICKS, RATIO;

    public Module(double MOTOR_TICKS, double RATIO, int side){ //left positive, right negative
        this.MOTOR_TICKS = MOTOR_TICKS;
        this.RATIO = RATIO;
        this.side = side;
    }
    public void readEncoders(double topTick, double bottomTick){
        tick = (topTick - bottomTick) / 2;
        rot = tick / MOTOR_TICKS / RATIO;
        angle = /*side * */Math.signum(rot)*(Math.abs(rot) - Math.floor(Math.abs(rot))) * 360.0;
    }
    public void calc(double y, double x, double rotation, double imu){
        strafe = Math.cos(Math.toRadians(imu))*x-Math.sin(Math.toRadians(imu))*y;
        forward = Math.sin(Math.toRadians(imu))*x+Math.cos(Math.toRadians(imu))*y - rotation*side;

        speed = Math.sqrt(Math.pow(strafe, 2) + Math.pow(forward, 2));
        setPivot(Math.toDegrees(Math.atan2(strafe, forward)));
    }

    public void setPivot(double target){
        pivot = Math.sin(Math.toRadians(angle - target));
        travel = Math.abs((angle+360)%360-(target+360)%360);
        toInverse = Math.min(travel, 360.0 - travel) > 90.0;
        if(!toInverse){
            //pivot *= side;
        }else{
            speed *= -1;
            pivot *= -1;
            //pivot*= -1;
        }
    }
    public double getPivot(){
        return pivot;
    }
    public double getAngle(){
        return angle;
    }
    public double getTravel(){
        return travel;
    }
    public boolean toInverse(){
        return toInverse();
    }
    public double getSpeed(){
        return speed;
    }
    public double gearSpeed(int hemisphere, int A, int B){
        return hemisphere * pivot * A - speed * B * side; //top negative, bottom positive
    }

}