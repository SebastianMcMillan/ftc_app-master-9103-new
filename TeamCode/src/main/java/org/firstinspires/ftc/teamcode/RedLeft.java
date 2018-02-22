package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Locale;

/*
 * This is an example LinearOpMode that shows how to use
 * the REV Robotics Color-Distance Sensor.
 *
 * It assumes the sensor is configured with the name "sensor_color_distance".
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 */
@Autonomous(name = "RedLeft", group = "Sensor")
//@Disabled                            // Comment this out to add to the opmode list
public class RedLeft extends LinearOpMode {

    /**
     * Note that the REV Robotics Color-Distance incorporates two sensors into one device.
     * It has a light/distance (range) sensor.  It also has an RGB color sensor.
     * The light/distance sensor saturates at around 2" (5cm).  This means that targets that are 2"
     * or closer will display the same value for distance/light detected.
     *
     * Although you configure a single REV Robotics Color-Distance sensor in your configuration file,
     * you can treat the sensor as two separate sensors that share the same name in your op mode.
     *
     * In this example, we represent the detected color by a hue, saturation, and value color
     * model (see https://en.wikipedia.org/wiki/HSL_and_HSV).  We change the background
     * color of the screen to match the detected color.
     *
     * In this example, we  also use the distance sensor to display the distance
     * to the target object.  Note that the distance sensor saturates at around 2" (5 cm).
     *
     */
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    ColorSensor sensorColor;

    private DcMotor backLeft; //Port 0 Hub 1
    private DcMotor backRight; //Port 1 Hub 1
    private DcMotor frontLeft; //Port 2 Hub 1
    private DcMotor frontRight; //Port 3 Hub 1

    private DcMotor relicMotor1; //Port 1 Hub 2
    private Servo relicServo2; //Port 2 Hub 2
    private CRServo relicServo3; //Port 3 Hub 2

    private DcMotor glyphMotor; //Port 0 Hub 2

    //Declare Servos
    private Servo glyphLeft; //Port 0 Hub 1
    private Servo glyphRight; //Port 1 Hub 1

    private Servo jewelServo; //Port 2 Hub 1

    VuforiaLocalizer vuforia;




    @Override

    public void runOpMode() throws InterruptedException{

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        glyphMotor = hardwareMap.dcMotor.get("glyphMotor");

        relicMotor1 = hardwareMap.dcMotor.get("relicMotor1");
        relicServo2 = hardwareMap.servo.get("relicServo2");
        relicServo3 = hardwareMap.crservo.get("relicServo3");

        //HW Map Servos
        glyphLeft = hardwareMap.servo.get("glyphLeft");
        glyphRight = hardwareMap.servo.get("glyphRight");

        jewelServo = hardwareMap.servo.get("jewelServo");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
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

        // get a reference to the distance sensor that shares the same name.

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


        double robotAngle = angles.secondAngle;
        ElapsedTime runtime = new ElapsedTime();

        composeTelemetry();
        telemetry.update();

        // wait for the start button to be pressed.
        waitForStart();

        glyphLeft.setPosition(0.9);
        glyphRight.setPosition(0.1);

        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        // loop and read the RGB and distance data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {
            // convert the RGB values to HSV values.
            // multiply by the SCALE_FACTOR.
            // then cast it back to int (SCALE_FACTOR is a double)
            Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                    (int) (sensorColor.green() * SCALE_FACTOR),
                    (int) (sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            // send the info back to driver station using telemetry function.
            telemetry.addData("Alpha", sensorColor.alpha());
            telemetry.addData("Red  ", sensorColor.red());
            telemetry.addData("Green", sensorColor.green());
            telemetry.addData("Blue ", sensorColor.blue());
            telemetry.addData("Hue", hsvValues[0]);

            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });

            telemetry.update();

            while (opModeIsActive() && robotAngle - 90 == robotAngle){

                frontRight.setPower(-0.5);
                frontLeft.setPower(0.5);
                backRight.setPower(-0.5);
                backLeft.setPower(0.5);
            }
            robotAngle = angles.secondAngle;

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 1.0)) {

                frontLeft.setPower(0.5);
                frontRight.setPower(0.5);
                backLeft.setPower(0.5);
                backRight.setPower(0.5);
            }
            runtime.reset();

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            while (opModeIsActive() && robotAngle + 90 == robotAngle){

                frontRight.setPower(0.5);
                frontLeft.setPower(-0.5);
                backRight.setPower(0.5);
                backLeft.setPower(-0.5);
            }
            robotAngle = angles.secondAngle;

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 1.0)) {

                frontLeft.setPower(-0.5);
                frontRight.setPower(-0.5);
                backLeft.setPower(-0.5);
                backRight.setPower(-0.5);
            }
            runtime.reset();

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            jewelServo.setPosition(0);
            if (hsvValues[0] < 35 && sensorColor.red() > sensorColor.blue()){
                telemetry.addLine("This is red");
                runtime.reset();
                while (opModeIsActive() && (runtime.seconds() < 0.5)) {

                    frontLeft.setPower(-0.5);
                    frontRight.setPower(0.5);
                    backLeft.setPower(-0.5);
                    backRight.setPower(0.5);
                }
                runtime.reset();

                while (opModeIsActive() && (runtime.seconds() < 0.5)) {

                    frontLeft.setPower(0.5);
                    frontRight.setPower(-0.5);
                    backLeft.setPower(0.5);
                    backRight.setPower(-0.5);
                }
                runtime.reset();
            }


            else if (hsvValues[0] >= 35 && sensorColor.red() <sensorColor.blue()){
                telemetry.addLine("This is blue");
                runtime.reset();
                while (opModeIsActive() && (runtime.seconds() < 0.5)) {

                    frontLeft.setPower(0.5);
                    frontRight.setPower(-0.5);
                    backLeft.setPower(0.5);
                    backRight.setPower(-0.5);
                }
                runtime.reset();

                while (opModeIsActive() && (runtime.seconds() < 0.5)) {

                    frontLeft.setPower(-0.5);
                    frontRight.setPower(0.5);
                    backLeft.setPower(-0.5);
                    backRight.setPower(0.5);
                }
                runtime.reset();
            }

            else{
                telemetry.addLine("What am I doing with my life?");
            }
            jewelServo.setPosition(1);

            while (opModeIsActive() && robotAngle - 90 == robotAngle){

                frontRight.setPower(0.5);
                frontLeft.setPower(-0.5);
                backRight.setPower(0.5);
                backLeft.setPower(-0.5);
            }
            robotAngle = angles.secondAngle;

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.5)) {

                frontLeft.setPower(0.5);
                frontRight.setPower(0.5);
                backLeft.setPower(0.5);
                backRight.setPower(0.5);
            }
            runtime.reset();

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
                telemetry.addData("VuMark", "is visible", vuMark);
            }

            else if (vuMark == RelicRecoveryVuMark.UNKNOWN) {

                telemetry.addData("VuMark", "is NOT visible", vuMark);
            }

            if (vuMark == RelicRecoveryVuMark.LEFT) {

                telemetry.addData("VuMark", "is Left", vuMark);
                vuforiaPosition = LEFT;
            }

            else if (vuMark == RelicRecoveryVuMark.CENTER) {

                telemetry.addData("VuMark", "is Center", vuMark);
                vuforiaPosition = CENTER;
            }

            else if (vuMark == RelicRecoveryVuMark.RIGHT) {

                telemetry.addData("VuMark", "is Right", vuMark);
                vuforiaPosition = RIGHT;
            }


            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < vuforiaPosition)) {

                frontLeft.setPower(0.5);
                frontRight.setPower(0.5);
                backLeft.setPower(0.5);
                backRight.setPower(0.5);
            }
            runtime.reset();

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            while (opModeIsActive() && robotAngle - 90 == robotAngle){

                frontRight.setPower(-0.5);
                frontLeft.setPower(0.5);
                backRight.setPower(-0.5);
                backLeft.setPower(0.5);
            }
            robotAngle = angles.secondAngle;

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.5)) {

                frontLeft.setPower(0.5);
                frontRight.setPower(0.5);
                backLeft.setPower(0.5);
                backRight.setPower(0.5);
            }
            runtime.reset();

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            glyphLeft.setPosition(0.7); // 0.7 is the open position for glyph servo Left
            glyphRight.setPosition(0.3); // 0.3 is the open position for glyph servo Right

        }

        // Set the panel back to the default color
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        });


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


