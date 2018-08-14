package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Locale;

@Autonomous(name = "NoGlyphRedLeft", group = "Sensor")

public class NoGlyphAutonRedLeft extends LinearOpMode {

    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    ColorSensor sensorColor;

    private DcMotor backLeft; //Port 0 Hub 1
    private DcMotor backRight; //Port 1 Hub 1
    private DcMotor frontLeft; //Port 2 Hub 1
    private DcMotor frontRight; //Port 3 Hub 1

    private DcMotor glyphMotor; //Port 0 Hub 2

    //Declare Servos
    private Servo glyphLeft; //Port 0 Hub 1
    private Servo glyphRight; //Port 1 Hub 1

    private Servo jewelServo; //Port 2 Hub 1

    VuforiaLocalizer vuforia;
    private Servo topGlyphRight;
    private Servo topGlyphLeft;




    @Override

    public void runOpMode() throws InterruptedException {

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        glyphMotor = hardwareMap.dcMotor.get("glyphMotor");

        //HW Map Servos
        glyphLeft = hardwareMap.servo.get("glyphLeft");
        glyphRight = hardwareMap.servo.get("glyphRight");

        jewelServo = hardwareMap.servo.get("jewelServo");

        topGlyphLeft = hardwareMap.servo.get("topGlyphLeft");
        topGlyphRight = hardwareMap.servo.get("topGlyphRight");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Set up our telemetry dashboard
        composeTelemetry();

        // get a reference to the color sensor.
        sensorColor = hardwareMap.get(ColorSensor.class, "colorSensor");


        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attentuate the measured values.
        final double SCALE_FACTOR = 255;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters1 = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters1.vuforiaLicenseKey = "AVrtV3L/////AAAAmY05oAXJSExMnfA9isTrlL9Wb5BjKpqdWP7WVu7w5TmtQciN1tKAo7JfLoEq0JJWXVjt2so3vtllp36RFQ3Wge/pC38H1yIQSVOs0W2CQ28XuBlqNAGAvvI8Bz8T3Ju/JbxmvWqt0+nZaEzMINHUVQOG3PgvXqizMCpdoyJVW54KG24h4m/Zq6F0AngRm54R5E/GKrVkzmUi/DuPy0ZKwzCKyBWBUKLaU4dpP+WWmRzOz3+IaRrxNOMCaHhZEKH0f55MEcAerGSODxV3YtLZw0+HzChjiFRFVX7WffU9uxu2w6/2RXdMEFrmdtsUuleOODHL8jw1kORgaMXglBk3/mYhjM4Vzj6yiqqKcJ3nQ8a8";
        parameters1.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters1);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        int vuforiaPosition = 0;
        int LEFT = 5;
        int RIGHT = 10;
        int CENTER = 15;

        angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double runtime = getRuntime();
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        double runtime2;
        double completed = 0;
        double FRpower = 0;
        double FLpower = 0;
        double BLpower = 0;
        double BRpower = 0;
        boolean red = false;
        boolean blue = false;
        double A_ServoValue = 1;
        double LIMpower = 0;
        double RIMpower = 1;
        double TRIMpower = 0;
        double TLIMpower = 1;
        int targetAngle = 0;


        composeTelemetry();
        telemetry.update();

        glyphLeft.setPosition(0.25);
        glyphRight.setPosition(0.7);
        topGlyphRight.setPosition(0.25);
        topGlyphLeft.setPosition(0.7);
        jewelServo.setPosition(1);

        waitForStart();

        while (opModeIsActive()) {

            Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                    (int) (sensorColor.green() * SCALE_FACTOR),
                    (int) (sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            telemetry.addData("Alpha", sensorColor.alpha());
            telemetry.addData("Red  ", sensorColor.red());
            telemetry.addData("Green", sensorColor.green());
            telemetry.addData("Blue ", sensorColor.blue());
            telemetry.addData("Hue", hsvValues[0]);

            angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            Color.RGBToHSV((int) (this.sensorColor.red() * SCALE_FACTOR),
                    (int) (this.sensorColor.green() * SCALE_FACTOR),
                    (int) (this.sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            getRuntime();

            runtime2 = getRuntime();

            /*if (completed != 1 ){
                runtimeme = getRuntime()();
                completed=1;
            }
            runtimeme2 = getRuntime()() - runtimeme;*/

            telemetry.addData("runtime2", getRuntime());

            telemetry.update();
// "pseudocode" for un-alligned arm: turn left, go forward, turn right, reverse, drop jewel manipulator, color sensor on right side do that stuff then turn right then forward to crypto then right forward stop release
            if (getRuntime() <= 3) { // literally does nothing for 3 secs

                FRpower = 0;
                FLpower = 0;
                BLpower = 0;
                BRpower = 0;
                A_ServoValue = 1;
            }

            /*if (getRuntime() > 3 && getRuntime() <= 5) { // turns left
                FRpower = .5;
                FLpower = 0;
                BLpower = 0;
                BRpower = .5;
            }

            if (getRuntime() > 5 && getRuntime() <= 7) { // goes forward
                FRpower = .5;
                FLpower = .5;
                BLpower = .5;
                BRpower = .5;
            }

            if (getRuntime() > 7 && getRuntime() <= 8) { // turns right
                FRpower = 0;
                FLpower = .5;
                BLpower = .5;
                BRpower = 0;
            }
*/
            if (getRuntime() > 8 && getRuntime() <= 9) { // reverses
                FRpower = -.5;
                FLpower = -.5;
                BLpower = -.5;
                BRpower = -.5;
            }
            if (getRuntime() > 9 && getRuntime() <= 10) { // stahp dat boi - k
                FRpower = 0;
                FLpower = 0;
                BLpower = 0;
                BRpower = 0;
            }

            if (getRuntime() > 10 && getRuntime() <= 15) { // does jewel things
                A_ServoValue = 0.4;
                if (hsvValues[0] < 35 && sensorColor.red() > sensorColor.blue()) {
                    telemetry.addLine("This is red");
                    red = true;


                } else if (hsvValues[0] >= 35 && sensorColor.red() < sensorColor.blue()) {
                    telemetry.addLine("This is blue");
                    blue = true;
                } else {
                    telemetry.addLine("What am I doing with my life?");
                }

                if (blue == true) {

                    FRpower = 0.5;
                    FLpower = -0.5;
                    BLpower = 0.5;
                    BRpower = -0.5;
                } else if (red == true) {

                    FRpower = -0.5;
                    FLpower = 0.5;
                    BLpower = -0.5;
                    BRpower = 0.5;
                } else {

                    FRpower = 0;
                    FLpower = 0;
                    BLpower = 0;
                    BRpower = 0;
                }
            }

            if (getRuntime() > 15 && getRuntime() <= 19) {

                A_ServoValue = 1;
                FRpower = .75;
                FLpower = .75;
                BLpower = .75;
                BRpower = .75;
            }

            if (getRuntime() > 19 && getRuntime() <= 20) {
                FRpower = 0;
                FLpower = 0;
                BRpower = 0;
                BLpower = 0;
            }

            if (getRuntime() > 20 && getRuntime() <= 21) {

                FRpower = 0;
                FLpower = .25;
                BRpower = 0;
                BLpower = .25;
            }

            if (getRuntime() > 21) {

                FRpower = 0;
                FLpower = 0;
                BRpower = 0;
                BLpower = 0;

                glyphLeft.setPosition(0); // 0.7 is the open position for glyph servo Left
                glyphRight.setPosition(1); // 0.3 is the open position for glyph servo Right
                topGlyphRight.setPosition(0);
                topGlyphLeft.setPosition(1);
            }

            //motors
            frontLeft.setPower(FLpower);
            frontRight.setPower(FRpower);
            backLeft.setPower(BLpower);
            backRight.setPower(BRpower);
            //servos
            jewelServo.setPosition(A_ServoValue);
            glyphLeft.setPosition(LIMpower);
            glyphRight.setPosition(RIMpower);
            topGlyphLeft.setPosition(TLIMpower);
            topGlyphRight.setPosition(TRIMpower);

        }
    }


    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------

    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel*gravity.xAccel
                                        + gravity.yAccel*gravity.yAccel
                                        + gravity.zAccel*gravity.zAccel));
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }





}


