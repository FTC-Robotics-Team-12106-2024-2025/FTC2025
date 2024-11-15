package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp

public class FieldCentric extends LinearOpMode {
     Gamepad driveGamepad = new Gamepad();
     Gamepad manipulatorGamepad = new Gamepad();

     // Variables
     public int armPose = 0;
     public double wrist = 4;
     public int slidePose = 0;
    @Override
    
    //Defines the motor
    public void runOpMode() {
        // Drive Motors
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft"); //Port 0
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight"); //Port 1
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft"); 
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");


        
       DcMotor armOne = hardwareMap.get(DcMotor.class,"armOne");
       Servo clawRotate = hardwareMap.get(Servo.class,"clawRotate");
       Servo clawClamp = hardwareMap.get(Servo.class,"clawClamp");
       DcMotor leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
       DcMotor rightSlide = hardwareMap.get(DcMotor.class,"rightSlide");

       // Setting arm position
       armOne.setTargetPosition(0);
       armOne.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       armOne.setPower(.65);
       leftSlide.setTargetPosition(0);
       leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       rightSlide.setTargetPosition(0);
       rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       rightSlide.setPower(.65);



        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(hardwareMap.appContext, R.raw.horn);

        waitForStart();

        if (isStopRequested()) {
            mediaPlayer.release();
            return;
        }
        
        while (opModeIsActive()) {
            // Getting inputs
            driveGamepad.copy(gamepad1);
            manipulatorGamepad.copy(gamepad2);

            //
            // Drive
            //
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

             boolean honk = driveGamepad.left_stick_button;

             //
            // Linear Slide
            //
             //For up-movement of linear slide
             double rotateRight = driveGamepad.right_trigger;
             double rotateLeft = driveGamepad.left_trigger;
             double clawPosX = manipulatorGamepad.left_stick_x;
             double clawPosY = -manipulatorGamepad.left_stick_y;

             boolean clawOpen = manipulatorGamepad.left_bumper;
             boolean clawClose = manipulatorGamepad.right_bumper;
             double wheelCPR = 423.2116; //Counts per revolution
             double linearCPR = 72.1;


             double currentHeading = imu.getAngularOrientation().firstAngle;
             double degreeOff = 0-currentHeading;
             double circleRadii = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));

             double xRot = x * Math.cos(degreeOff) - y * Math.sin(degreeOff);
             double yRot = y * Math.cos(degreeOff) + x * Math.sin(degreeOff);

             double combinedRotation = .85*(rotateRight-rotateLeft);
             double fl = (yRot+xRot+combinedRotation);
             double fr = (xRot-yRot-combinedRotation);
             double bl = (yRot-xRot-combinedRotation);
             double br = (-yRot-xRot+combinedRotation);

            //stops it from going greater than 1/-1
             double maxNumber = Math.max(Math.abs(xRot)+Math.abs(yRot)+Math.abs(combinedRotation),1);
             //powers the motor for wheels
            frontLeft.setPower(fl/maxNumber*0.5);
            frontRight.setPower(fr/maxNumber*0.5);
            backLeft.setPower(bl/maxNumber*0.5);
            backRight.setPower(br/maxNumber*0.5);

            if (driveGamepad.guide) {
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
                else {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
            }
        //temp code

        //Arm Pose
            
        if (manipulatorGamepad.dpad_up) {
            armPose -= 5;
        }
        if (manipulatorGamepad.dpad_down) {
            armPose += 5;
        }
        if (manipulatorGamepad.dpad_right) {
            armPose--;
        }
        if (manipulatorGamepad.dpad_left) {
            armPose++;
        }
        if (armPose <= -126) {
            armPose = -126;
        }
        if (armPose >= -5) {
            armPose = -5;
        }
        
        
        if (manipulatorGamepad.cross) {
            armPose = -7;
        }
        if (manipulatorGamepad.triangle) {
            armPose = -126;
        }
        if (manipulatorGamepad.square) {
            armPose = -72;
        }
            //Slide Movement
            if (driveGamepad.dpad_up) {
                slidePose -= 5;
            }
            if (driveGamepad.dpad_down) {
                slidePose += 5;
            }
            if (driveGamepad.dpad_right) {
                slidePose--;
            }
            if (driveGamepad.dpad_left) {
                slidePose++;
            }
            if (slidePose <= -126) {
                slidePose = -126;
            }
            if (slidePose >= -5) {
                slidePose = -5;
            }

            if (driveGamepad.cross) {
                slidePose = -7;
            }
            if (driveGamepad.triangle) {
                slidePose = -126;
            }
            if (driveGamepad.square) {
                slidePose = -72;
            }

            armOne.setTargetPosition(armPose);
            leftSlide.setTargetPosition(slidePose);
            rightSlide.setTargetPosition(slidePose);

            telemetry.addData("Arm One: ", armOne.getCurrentPosition());
            telemetry.addData("Left Slide: ", leftSlide.getCurrentPosition());
            telemetry.addData("Right Slide: ", rightSlide.getCurrentPosition());





       
       if (clawOpen) {
           clawClamp.setPosition(1);
       }
       if (clawClose) {
           clawClamp.setPosition(0);
       }
       
       if (manipulatorGamepad.left_trigger >= .9) {
           wrist -= .05;
       }
       if (manipulatorGamepad.right_trigger >= .9) {
           wrist += .05;
       }
       if (wrist >= 1) {
           wrist = 1;
       }
       if (wrist <= 0) {
           wrist = 0;
       }
       if (manipulatorGamepad.circle) {
           wrist = .4;
       }
       clawRotate.setPosition(wrist);


         
  

        telemetry.update();
    
        


        
        
        
        
        
        
        }
    }
}



