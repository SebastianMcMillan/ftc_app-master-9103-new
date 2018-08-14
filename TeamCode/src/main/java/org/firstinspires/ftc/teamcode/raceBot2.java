package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name = "raceBot2", group = "Linear OpMode")

public class raceBot2 extends LinearOpMode {

    //Declare OpMode Members
    private ElapsedTime runtime = new ElapsedTime();

    //Declare Motor Controllers
    private DcMotorController right;
    private DcMotorController left;


    //Declare Motors
    private DcMotor backLeft; //Port 1 On Controller dalek
    private DcMotor backRight; //Port 2 On Controller dalek
    private DcMotor frontLeft; //Port 1 On Controller redMage
    private DcMotor frontRight; //Port 2 On Controller redMage



    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //HW Map Motors on Controller redMage
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");

        //HW Map Motors on Controller dalek
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);



        //Set Arm Motor to encoder mode

        //glyphArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();
        runtime.reset();

        //Run until end of match (when driver presses stop)
        while (opModeIsActive()) {
            double leftPower;
            double rightPower;

            if (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0) /*If the left stick is not neutral...*/ {
                double drive = -gamepad1.left_stick_y; //Set the Drive to the negative value of the y-axis value
                double turn = gamepad1.left_stick_x; //Set the turn to the value of the x-axis value

                leftPower = Range.clip(drive + turn, -1.0, 1.0); //fun math
                rightPower = Range.clip(drive - turn, -1.0, 1.0); //fun math 2

                frontLeft.setPower(leftPower); //These are hopefully self-explanatory.
                frontRight.setPower(rightPower);
                backLeft.setPower(leftPower);
                backRight.setPower(rightPower);
            }


            //If The Left Stick and the Triggers are not being pressed, Stop 'dem motors
            if (gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0 && gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0)
            {
                frontLeft.setPower(0.0);
                frontRight.setPower(0.0);
                backLeft.setPower(0.0);
                backRight.setPower(0.0);
            }

            telemetry.update();

        }
    }
}
