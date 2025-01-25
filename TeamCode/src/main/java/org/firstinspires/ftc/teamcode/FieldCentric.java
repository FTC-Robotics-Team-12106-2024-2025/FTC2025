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

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp

public class FieldCentric extends LinearOpMode {
     Gamepad driveGamepad = new Gamepad();
     Gamepad manipulatorGamepad = new Gamepad();

     // Variables

     public int armPos = 0;
     public int slidePos = 0;
     public float wristPos = 0.5f;

     int colorFilter = 0; // 0 = filter for red, 1 = for blue, 2 = for yellow

     public int armUpPos = -718;//change this value according to encoder
     public int armDownPos = -1800;//change this value according to encoder
     public int targetArmPos = 0;//updates in if statement, DO NOT CHANGE



    @Override

    //Defines the motor
    public void runOpMode() {
        // Drive Motors
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft"); // Port 0
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight"); //Port 1
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");

        DcMotor armMotor = hardwareMap.get(DcMotor.class,"armMotor");
        DcMotor slideMotor = hardwareMap.get(DcMotor.class, "slide");
        CRServo intakeOne = hardwareMap.get(CRServo.class,"intakeOne");
        CRServo intakeTwo = hardwareMap.get(CRServo.class,"intakeTwo");
        Servo wristServo = hardwareMap.get(Servo.class,"wrist");

        ColorSensor color = hardwareMap.get(ColorSensor.class, "color");
        DistanceSensor colorDistance = hardwareMap.get(DistanceSensor.class, "color");
        ElapsedTime timer = new ElapsedTime();
        boolean sortActive = false;

        //BRAKES
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //encoders
        //slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       // jointMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Resetting encoders
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

//        // Setting Positions
       slideMotor.setTargetPosition(slidePos);
      slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       slideMotor.setPower(.8);

        armMotor.setTargetPosition(targetArmPos);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(.8);

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

//            jointMotor.setTargetPosition(0);
//            jointMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            jointMotor.setPower(.8);

            // Setting Positions
//        slide.setTargetPosition(0);
//        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        slide.setPower(.8);



            //
            // Drive
            //
            float y = -driveGamepad.left_stick_y;
            float x = driveGamepad.left_stick_x;
            float slowX = driveGamepad.right_stick_x/4;

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
            double rx = rotateRight-rotateLeft+slowX;
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
            frontLeft.setPower(fl / maxNumber * .7);
            frontRight.setPower(fr / maxNumber * 7);
            backLeft.setPower(bl / maxNumber * .7);
            backRight.setPower(br / maxNumber * .7);

            //
            // Arm Angle
            //

            armPos += (-manipulatorGamepad.right_stick_y * 100);

            if (armPos > 0) {
                armPos = 0;
            }
            if (armPos < -1800) {
                armPos = -1800;
            }

            if (manipulatorGamepad.dpad_up) {
                armPos = armUpPos;
                wristPos = 0;

            } else if (manipulatorGamepad.dpad_down) {
                armPos = armDownPos;

            }

            armMotor.setTargetPosition(armPos);
            telemetry.addData("Arm Angular Position: ", armPos);


            //
            // Linear Slide
            //
            slidePos += (-manipulatorGamepad.left_stick_y * 100);

            // Limits
            if (slidePos < 0) {
                slidePos = 0;
            }
            if (slidePos > 6100) {
                slidePos = 6100;
            }

            slideMotor.setTargetPosition(slidePos);
            telemetry.addData("Slide Linear Position: ", slidePos);

            //
            // Wrist
            //
            if (manipulatorGamepad.left_bumper) {
                wristPos = 1;
            }
            if (manipulatorGamepad.right_bumper) {
                wristPos = 0;
            }
            if (manipulatorGamepad.guide) {
                wristPos = 0.5f;
            }

            wristServo.setPosition(wristPos);
            telemetry.addData("WristPose", wristPos);

            //
            // Intake
            //
            if (manipulatorGamepad.left_trigger >= .9) {
                intakeOne.setPower(-0.75);
                intakeTwo.setPower(0.75);
            }
           if (manipulatorGamepad.right_trigger >= .9) {
               intakeOne.setPower(0.75);
               intakeTwo.setPower(-0.75);
           }

           if (manipulatorGamepad.options){
               sortActive = !sortActive;
           }


           if (manipulatorGamepad.circle){ // for async
               intakeOne.setPower(0);
               intakeTwo.setPower(0);
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



