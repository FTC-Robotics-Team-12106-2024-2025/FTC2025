package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;


public class AutonLibrary  {
    public DcMotor frontLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft = null;
    public DcMotor backRight = null;
    public DcMotor leftSlide = null;
    public DcMotor Arm = null;
    public Servo clawClamp = null;
    public Servo clawRotate = null;

    public void moveWheel(double x,double y) {
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
        //Should do telemetery data thingy I hate this.

    }
   public void turn(boolean rotateLeft, boolean rotateRight) {
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
    } 
    public void linearVertical(int verticalPose) {
        leftSlide.setTargetPosition(0);
        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftSlide.setPower(0.65);
        leftSlide.setTargetPosition(verticalPose);
    }
    public void clawGrabber(float clawPose) {
        if (clawPose>0) {
            clawClamp.setPosition(clawPose);
        }
        else if (clawPose <= 0) {
            clawClamp.setPosition(clawPose);
        }
    }
    public void clawRot(float clawAngle) {
        if (clawAngle>0) {
            clawRotate.setPosition(clawAngle);
        }
    }
    public void armMovement(int armPose) {
        Arm.setTargetPosition(0);
        Arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Arm.setPower(0.65);
        Arm.setTargetPosition(armPose);
    }

        //
        //Movement Code
        //

        public void moveForward(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100); //Im using tenths of a second
        while (System. currentTimeMillis() < stop) {
            //not even sure if this works or not. We can test it anyway
            moveWheel(0,0.5);
        }
    }
        public void moveBackward(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
            while (System. currentTimeMillis() < stop) {
                //not even sure if this works or not. We can test it anyway
                moveWheel(0,-0.5);
        }
    }
    //Hopefully strafe code works
        public void strafeRight(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
            while (System. currentTimeMillis() < stop) {
                //not even sure if this works or not. We can test it anyway
                moveWheel(0.5,0);
        }
    }
        public void strafeLeft(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
                while (System. currentTimeMillis() < stop) {
                //not even sure if this works or not. We can test it anyway
                moveWheel(-0.5,0);
        }
    }
       public void rotateLeft(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
                while (System. currentTimeMillis() < stop) {
                turn(true,false);
        }
    }
    public void rotateRight(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
                while (System. currentTimeMillis() < stop) {
                turn(false,true);
        }
    }

    //
    //Claw Code
    //

    public void clawOpen(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            clawGrabber(1);
        }
    }
    public void clawClose(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            clawGrabber(0);
        }
    }
    public void clawZero(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            clawRot(0);
        }
    }
    public void clawNinety(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            clawRot(0.25F);
        }
    }
    public void clawOneEighty(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            clawRot(0.5F);
        }
    }
    public void clawTwoSeventy(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            clawRot(0.75F);
        }
    }

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
            linearVertical(1627);
        }
    }
    public void slideHalf(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(3255);
        }
    }
    public void slideThreeQuarts(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(4883);
        }
    }
    public void slideMax(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(6510);
        }
    }

    //
    //Arm Code
    //
    public void armZero(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            armMovement(0);
        }
    }
    public void armQuarter(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            armMovement(458);
        }
    }
    public void armHalf(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            armMovement(916);
        }
    }
    public void armThreeQuarts(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            armMovement(1375);
        }
    }
    public void armMax(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            armMovement(1833);
        }
    }

}
