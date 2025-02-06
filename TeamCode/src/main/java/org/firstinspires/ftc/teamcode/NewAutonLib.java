package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.hardware.bosch.BNO055IMU;


public class NewAutonLib extends LinearOpMode {
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;
    public DcMotor slide;
    public DcMotor armMotor;
    public CRServo intakeOne;
    public CRServo intakeTwo;
    public ColorSensor color;
    public DistanceSensor colorDistance;
    public ElapsedTime timer;
    public Servo wrist;
    public BNO055IMU imu;
    public double currentHeading;

    public void runOpMode() {
        //
        // Initializing
        //
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        slide = hardwareMap.get(DcMotor.class, "slide");
        armMotor = hardwareMap.get(DcMotor.class,"jointMotor");
        intakeOne = hardwareMap.get(CRServo.class,"intakeOne");
        intakeTwo = hardwareMap.get(CRServo.class,"intakeTwo");
        wrist = hardwareMap.get(Servo.class,"wrist");

        int[][] thresholds = {{1,1,1},{1,1,1},{1,1,1}};// ind 0 = red, ind 1 = blue, ind 2 = green; List of thresholds per sample; red, blue, yellow
        String[] colorData = {"Filtering for RED samples","Filtering for BLUE samples","Filtering for YELLOW samples"};
        int[][] sampleHave = {{211,41,37},{55,89,223},{241,178,14}};
        int[][] sampleLook = {{233,141,138},{155,172,239},{246, 217, 135}};

        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        waitForStart();

        currentHeading = -imu.getAngularOrientation().firstAngle;

        autonCommands();
    }

    /**
     * Method to be overriden with all the auton commands.
     */
    public void autonCommands() {

    }

    //
    // Planar Movement
    //

    /**
     * @param x_f horizontal displacement from starting point
     * @param y_f vertical displacement from starting pont
     * @param theta_f angular displacement from starting point
     * @param time seconds
     */
    public final void strafeRight(double x_f, double y_f, double theta_f, int time) {
        long time_c = System.currentTimeMillis(); // current time
        long time_f = time_c + (time * 1000); // ending time

        double changeX, changeY, changeTheta;
        double xDisplace, yDisplace;
        xDisplace = yDisplace = 0;
        float xPow, yPow, thetaPow;
        double xRot, yRot;
        double fl, fr, br, bl;

        while (time_c < time_f) {
            if (isStopRequested()) {
                stop();
            }

            currentHeading = -imu.getAngularOrientation().firstAngle;
            changeTheta = (theta_f - currentHeading) / (time_f - time_c);
            thetaPow = (1 / (float) Math.PI) * (float) changeTheta;

            changeX = (x_f - xDisplace) / (time_f - time_c);
            xPow = (float) (2 / (1 + Math.exp(-3.5 * changeX))) - 1;
            xDisplace += changeX;

            changeY = (y_f - yDisplace) / (time_f - time_c);
//            yPow = (1f / 6) * (float) changeY;
            yPow = (float) (2 / (1 + Math.exp(-3.5 * changeY))) - 1; // logistic growth sob
            yDisplace += changeY;

            xRot = xPow * Math.cos(currentHeading) - yPow * Math.sin(currentHeading);
            yRot = yPow * Math.cos(currentHeading) + xPow * Math.sin(currentHeading);

            //stops it from going greater than 1/-1
            double maxNumber = Math.max(Math.abs(xPow) + Math.abs(yPow) + Math.abs(thetaPow), 1);

            fl = (yRot + xRot + thetaPow);
            fr = (xRot - yRot + thetaPow);
            bl = (yRot - xRot + thetaPow);
            br = (-yRot - xRot + thetaPow);

            //powers the motor for wheels
            frontLeft.setPower(fl / maxNumber);
            frontRight.setPower(fr / maxNumber);
            backLeft.setPower(bl / maxNumber);
            backRight.setPower(br / maxNumber);

            time_c = System.currentTimeMillis();
        }
    }

    //
    // Angular Movement
    //


}
