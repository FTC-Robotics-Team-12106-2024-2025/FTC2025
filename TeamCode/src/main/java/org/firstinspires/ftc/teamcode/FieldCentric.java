package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp

public class FieldCentric extends LinearOpMode {
     Gamepad driveGamepad = new Gamepad();
     Gamepad manipulatorGamepad = new Gamepad();

     // Variables

     public int jointPose = 0;
     public int slidePose = 0;
     public float wristPose = 0;
     public int Test;


     int colorFilter = 0; // 0 = filter for red, 1 = for blue, 2 = for yellow

     public int jointLiftUpPosition = -8000;//change this value according to encoder
     public int jointLiftDownPosition = 500;//change this value according to encoder
     public int targetLiftPosition = 0;//updates in if statement, DO NOT CHANGE



    @Override

    //Defines the motor
    public void runOpMode() {
        // Drive Motors
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft"); //Port 0
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight"); //Port 1
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");

        DcMotor jointMotor = hardwareMap.get(DcMotor.class,"jointMotor");
        DcMotor slide = hardwareMap.get(DcMotor.class, "slide");
        CRServo intakeOne = hardwareMap.get(CRServo.class,"intakeOne");
        CRServo intakeTwo = hardwareMap.get(CRServo.class,"intakeTwo");
        Servo wrist = hardwareMap.get(Servo.class,"wrist");

        ColorSensor color = hardwareMap.get(ColorSensor.class, "color");
        DistanceSensor colorDistance = hardwareMap.get(DistanceSensor.class, "color");
        ElapsedTime timer = new ElapsedTime();
        boolean sortActive = false;

        //BRAKES
        jointMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //encoders
        //slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       // jointMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//slide
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //joint
        jointMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

//        // Setting Positions
//        slide.setTargetPosition(targetLiftPosition);
//        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        slide.setPower(.8);
//
//        jointMotor.setTargetPosition(targetLiftPosition);
//        jointMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        jointMotor.setPower(.8);

        // Gyroscope
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        //All arrays based off the colorFilter variable
        int[][] thresholds = {{130,40,40},{30,50,40},{170,60,100}};// ind 0 = red, ind 1 = blue, ind 2 = green; List of thresholds per sample; red, blue, yellow
        String[] colorData = {"Filtering for RED samples","Filtering for BLUE samples","Filtering for YELLOW samples"};
        int[][] sampleHave = {{211,41,37},{55,89,223},{241,178,14}};
        int[][] sampleLook = {{233,141,138},{155,172,239},{246, 217, 135}};

        waitForStart();

        if (isStopRequested()) {
            return;
        }
        
        while (opModeIsActive()) {
            // Getting inputs
            driveGamepad.copy(gamepad1);
            manipulatorGamepad.copy(gamepad2);

            Test = jointMotor.getCurrentPosition();


//            jointMotor.setTargetPosition(0);
//            jointMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            jointMotor.setPower(.8);

            // Setting Positions
//        slide.setTargetPosition(0);
//        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        slide.setPower(.8);

        jointMotor.setTargetPosition(targetLiftPosition);
        jointMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        jointMotor.setPower(.8);

            if (manipulatorGamepad.dpad_up) {
                targetLiftPosition = jointLiftUpPosition;

            } else if (manipulatorGamepad.dpad_down) {
                targetLiftPosition = jointLiftDownPosition;

            }

            //
            // Drive
            //
            double wheelCPR = 423.2116; //Counts per revolution
            double linearCPR = 72.1;
            float y = -driveGamepad.left_stick_y;
            float x = driveGamepad.left_stick_x;

            if (driveGamepad.dpad_left) {
                 x = -0.25f;
            }
            if (driveGamepad.dpad_right) {
                 x = 0.25f;
            }
            if (driveGamepad.dpad_up) {
                y = 0.25f;
            }
            if (driveGamepad.dpad_down) {
                 y = -0.25f;
            }

            double currentHeading = -imu.getAngularOrientation().firstAngle;
//          boolean honk = driveGamepad.left_stick_button;
            double rotateRight = driveGamepad.right_trigger;
            double rotateLeft = driveGamepad.left_trigger;
            double rx = rotateRight-rotateLeft;
            double xRot = x * Math.cos(currentHeading) - y * Math.sin(currentHeading);
            double yRot = y * Math.cos(currentHeading) + x * Math.sin(currentHeading);

            // Motor powers
            double fl = (yRot+xRot+rx);
            double fr = (xRot-yRot+rx);
            double bl = (yRot-xRot+rx);
            double br = (-yRot-xRot+rx);

            //stops it from going greater than 1/-1
            double maxNumber = Math.max(Math.abs(xRot)+Math.abs(yRot)+Math.abs(rx),1);

            //powers the motor for wheels
            frontLeft.setPower(fl / maxNumber * .85);
            frontRight.setPower(fr / maxNumber * .85);
            backLeft.setPower(bl / maxNumber * .85);
            backRight.setPower(br / maxNumber * .85);

            //
            // Arm Angle (Joint)
            //

            jointPose += (-manipulatorGamepad.right_stick_y * 100);

            if (jointPose > 0) {
                jointPose = 0;
            }
            if (jointPose < -1570) {
                jointPose = -1570;
            }

            jointMotor.setTargetPosition(jointPose);
            telemetry.addData("Measured Arm Position: ", jointPose);


            //
            // Linear Slide
            //
            slidePose += (-manipulatorGamepad.left_stick_y * 100);

            // Limits
            if (slidePose < 0) {
                slidePose = 0;
            }
            if (slidePose > 6100) {
                slidePose = 6100;
            }

            //Joint
           telemetry.addData("jointP", jointPose);

            slide.setTargetPosition(slidePose);
            telemetry.addData("SlidePose: ", slidePose);

            //
            // Wrist
            //
            if (manipulatorGamepad.left_bumper) {
                wristPose += 0.1f;
            }
            if (manipulatorGamepad.right_bumper) {
                wristPose -= 0.1f;
            }

            if (wristPose < 0) {
                wristPose = 0;
            }
            if (wristPose > 1) {
                wristPose = 1;
            }
            wrist.setPosition(wristPose);
            telemetry.addData("WristPose",wristPose);

            //
            // Intake
            //
            if (manipulatorGamepad.left_trigger >= .9) {
                intakeOne.setPower(0.75);
                intakeTwo.setPower(-0.75);
            }
           if (manipulatorGamepad.right_trigger >= .9) {
               intakeOne.setPower(-0.75);
               intakeTwo.setPower(0.75);
           }
           //Emergency Stop
           if (manipulatorGamepad.options) {
               intakeOne.setPower(0);
               intakeTwo.setPower(0);
           }
           if (manipulatorGamepad.circle){ // for async
               sortActive = false;
           }

           if (sortActive) {
               manipulatorGamepad.setLedColor(sampleLook[colorFilter][0], sampleLook[colorFilter][1], sampleLook[colorFilter][2], 1000);
               intakeOne.setPower(-0.75);
               intakeTwo.setPower(-0.75); // powers might be wrong
               int red = thresholds[colorFilter][0];
               int blue = thresholds[colorFilter][1];
               int green = thresholds[colorFilter][2];
               if (colorDistance.getDistance(DistanceUnit.CM) < 2.0) {
                   intakeOne.setPower(0);
                   intakeTwo.setPower(0);           // tolerance of 10 (can be changed)
                   if (Math.abs(color.red() - red) < 10 && Math.abs(color.blue() - blue) < 10 && Math.abs(color.green() - green) < 10) {
                       driveGamepad.rumble(50);
                       manipulatorGamepad.rumble(50);
                       manipulatorGamepad.setLedColor(sampleHave[colorFilter][0], sampleHave[colorFilter][1], sampleHave[colorFilter][2], 1000);
                       sortActive = false;
                   } else {
                       timer.reset();// might reset too much
                       intakeOne.setPower(0.75);
                       intakeTwo.setPower(0.75);
                       if (timer.milliseconds() >= 500) { // might not resolve if it checks it immediately (checks right after timer is on and never flips direction)
                           intakeOne.setPower(-0.75);
                           intakeTwo.setPower(-0.75);
                       }
                   }
               }
           }
           //ColorSort

           if (manipulatorGamepad.share) {
               colorFilter++;
               if (colorFilter > 2) {
                   colorFilter = 0;
               }
               telemetry.clearAll();// Updates which type of color sort it is; other info will be printed due to it being a while loop
               telemetry.addData(colorData[colorFilter], colorFilter);
               telemetry.update();
           }

            telemetry.addData("Red",color.red());
            telemetry.addData("Blue",color.blue());
            telemetry.addData("Green",color.green());
            telemetry.update();
        }
    }
}



