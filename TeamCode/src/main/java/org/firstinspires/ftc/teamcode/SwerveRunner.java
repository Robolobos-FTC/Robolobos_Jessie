package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.*;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.*;

@TeleOp(name="Runner", group="DriveModes")
public class SwerveRunner extends OpMode {
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
    private double x, y, rotation, lateral, angle, TLv, TRv, BLv, BRv;
    private final int A = 1000;
    private final int B = 1000;
    private ElapsedTime runtime;
    private BNO055IMU imu;
    SwerveModule left, right;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");
        runtime = new ElapsedTime();

        TL = hardwareMap.get(DcMotorEx.class, "TL"); //1
        BL = hardwareMap.get(DcMotorEx.class, "BL"); //1
        TR = hardwareMap.get(DcMotorEx.class, "TR"); //1
        BR = hardwareMap.get(DcMotorEx.class, "BR"); //1

        TL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //TL.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.REVERSE);
        //TR.setDirection(DcMotorSimple.Direction.REVERSE);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);

        left = new SwerveModule(145.1, 106.0/13 * 18.0/14, -1);
        right = new SwerveModule(145.1, 106.0/13 * 18.0/14, 1);

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
        y = Math.abs(gamepad1.left_stick_y) > 0.01 ? gamepad1.left_stick_y : 0; //forward movement
        rotation = gamepad1.right_stick_x; //rotational movement

        left.setPose(TL.getCurrentPosition(), BL.getCurrentPosition());
        right.setPose(TR.getCurrentPosition(), BR.getCurrentPosition());

        lateral = Math.sqrt(x*x + y*y);
        if(x != 0 || y != 0) angle = Math.toDegrees(Math.atan2(x, y))/* - imu.getAngularOrientation().firstAngle*/; //Degree of lateral movement stick, forward is 0, right is 90

        left.setPivot(angle);
        right.setPivot(angle);

        TLv = (-left.getPivot() * A + 0.5
                *
                (lateral-rotation) * B * left.toInverse());
        BLv = (left.getPivot() * A + 0.5
                *
                (lateral-rotation) * B * left.toInverse());
        TRv = (-right.getPivot() * A - 0.5
                *
                (lateral+rotation) * B * right.toInverse());
        BRv = (right.getPivot() * A - 0.5
                *
                (lateral+rotation) * B * right.toInverse());

        //double high = Math.max(Math.abs(TRv), Math.abs(BRv));
        //TRv /= Math.max(maxV, high);
        //BRv /= Math.max(maxV, high);

        TL.setVelocity(TLv);
        BL.setVelocity(BLv);
        TR.setVelocity(TRv);
        BR.setVelocity(BRv);

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Joystick: ", Math.floor(angle * 100) / 100);
        telemetry.addData("Wheels:   ", Math.floor(right.getAngle() * 100) / 100);
        telemetry.addData("TR V:     ", Math.floor(TR.getVelocity() * 100) / 100);
        telemetry.addData("BR V:     ", Math.floor(BR.getVelocity() * 100) / 100);
        telemetry.addData("Ang Diff: ", Math.floor(Math.min(left.getTravel(), 360.0 - left.getTravel()) * 100) / 100);
        telemetry.addData("Heading:  ", imu.getAngularOrientation().firstAngle);
    }
    @Override
    public void stop() {
    }
}
class SwerveModule {

    private double angle, rot, topV, bottomV, pivot, tick, travel;
    private int curTick, inverse, side; //left = 1, right = -1
    private boolean toInverse;
    private double MOTOR_TICKS;
    private double RATIO;

    public SwerveModule(double MOTOR_TICKS, double RATIO, int side){
        this.MOTOR_TICKS = MOTOR_TICKS;
        this.RATIO = RATIO;
        this.side = side;
    }
    public void setPose(double topTick, double bottomTick){
        tick = (topTick - bottomTick) / 2;
        rot = tick / MOTOR_TICKS / RATIO;
        angle = side * Math.signum(rot)*(Math.abs(rot) - Math.floor(Math.abs(rot))) * 360.0;
    }
    public void setPivot(double joystick){
        pivot = Math.sin(Math.toRadians(angle - joystick));
        travel = Math.abs((angle+360)%360-(joystick+360)%360);
        toInverse = Math.min(travel, 360.0 - travel) >= 90.0;
        if(!toInverse){
            inverse = 1;
            pivot *= side;
        }else{
            inverse = -1;
            pivot *= -side;
        }
    }
    public double getPivot(){
        return pivot;
    }
    public double getTravel(){
        return travel;
    }
    public int toInverse(){
        if(toInverse) return -1;
        return 1;
    }
    public double getAngle(){
        return angle;
    }
}
