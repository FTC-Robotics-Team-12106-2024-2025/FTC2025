package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

// Strafe 20 for half a tile, 40 for a tile

public class AutonLibrary extends LinearOpMode {
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;
    public DcMotor slide;
    public Servo joint;
    public CRServo intakeOne;
    public CRServo intakeTwo;

    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        slide = hardwareMap.get(DcMotor.class, "leftSlide");
        joint = hardwareMap.get(Servo.class,"joint");

        waitForStart();

        autonCommands();
    }

    public void autonCommands() {

    }

    public final void moveWheel(double x,double y) {
         double fl = (y+x);
         double fr = (x-y);
         double bl = (y-x);
         double br = (-y-x);
        //stops it from going greater than 1/-1
         double maxNumber = Math.max(Math.abs(x)+Math.abs(y),1);
         //powers the motor for wheels
        frontLeft.setPower(fl/maxNumber);
        frontRight.setPower(fr/maxNumber);
        backLeft.setPower(bl/maxNumber);
        backRight.setPower(br/maxNumber); 
        //Should do telemetery dataz thingy I hate this.

    }
   public final void turn(boolean rotateLeft, boolean rotateRight) {
        double fl, fr, bl, br;

        fl = fr = bl = br = 0;
        if (rotateRight) {
            fl = 0.5;
            fr = 0.5;
            bl = 0.5;
            br = 0.5;
         }
         if (rotateLeft) {
             fl = -0.5;
             fr = -0.5;
             bl = -0.5;
             br = -0.5;
         }
         frontLeft.setPower(fl);
         frontRight.setPower(fr);
         backLeft.setPower(bl);
         backRight.setPower(br);
   }

   public final void linearVertical(int verticalPose) {
        slide.setTargetPosition(0);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setPower(0.65);
        slide.setTargetPosition(verticalPose);
    }
//Write the Intake Code Here


    public final void jointMovement(double jointPose) {
        joint.setPosition(jointPose);
    }

        //
        //Movement Code
        //

    public final void moveForward (int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100); //Im using tenths of a second
        while (System. currentTimeMillis() < stop) {
            //not even sure if this works or not. We can test it anyway
            moveWheel(0,0.5);
        }
    }
    public final void moveBackward(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
            while (System. currentTimeMillis() < stop) {
                //not even sure if this works or not. We can test it anyway
                moveWheel(0,-0.5);
        }
    }
    //

    //Hopefully strafe code works
    public final void strafeRight(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
            while (System. currentTimeMillis() < stop) {
                //not even sure if this works or not. We can test it anyway
                moveWheel(0.5,0);
        }
    }
    public final void strafeLeft(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
                while (System. currentTimeMillis() < stop) {
                //not even sure if this works or not. We can test it anyway
                moveWheel(-0.5,0);
        }
    }
       public final void rotateLeft(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
                while (System. currentTimeMillis() < stop) {
                turn(true,false);
        }
    }
    public final void rotateRight(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
                while (System. currentTimeMillis() < stop) {
                turn(false,true);
        }
    }
    public final void killSwitch(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            //not even sure if this works or not. We can test it anyway
            moveWheel(0,0);
        }
    }

    //
    //Claw Code (Write It Here)
    //



    //
    //Slide Code
    //

    public void slideZero(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(0);
        }
    }
    public void slideQuarter(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(1525);
        }
    }
    public void slideHalf(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(3050);
        }
    }
    public void slideThreeQuarts(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(4575);
        }
    }
    public void slideMax(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(6100);
        }
    }

    //
    //Arm Code
    //

    public void jointZero(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            jointMovement(0);
        }
    }
    public void jointQuarter(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            jointMovement(0.25);
        }
    }
    public void jointHalf(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths * 100);
        while (System.currentTimeMillis() < stop) {
            jointMovement(0.5);
        }
    }
    public void jointThreeQuarts(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            jointMovement(0.75);
        }
    }
    public void jointMax(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            jointMovement(1);
        }
    }

}
