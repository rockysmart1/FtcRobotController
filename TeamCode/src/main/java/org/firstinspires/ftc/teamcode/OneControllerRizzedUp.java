package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "OneControllerRizzedUp")
public class OneControllerRizzedUp extends LinearOpMode {

    public boolean clawOpen = true;
    public boolean clawPressed = false;

    @Override
    public void runOpMode() {
        double speedE;
        double forward;
        double strafe;
        double turn;
        double denominator;
        double armPowerVariable;
        double arm_power;


        Servo servo = hardwareMap.get(Servo.class, "arm servo!");
        DcMotor armMotor = hardwareMap.get(DcMotor.class, "Arm Motor");
        DcMotor leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        DcMotor rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        DcMotor leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        DcMotor rightRear = hardwareMap.get(DcMotor.class, "rightRear");

        // Put initialization blocks here.
        waitForStart();
        // 0.2 before 0.1 to try make open wider
        servo.setPosition(0.1);

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                //set speed
                if (gamepad1.right_bumper)
                {
                    armPowerVariable = 0.15;
                    speedE = 0.25;
                }
                else
                {
                    speedE = 1;
                    armPowerVariable = 2;
                }



                //servo
                if ((gamepad1.a && !clawPressed))
                {
                    telemetry.addData("a pressed:", true);
                    clawOpen = !clawOpen;
                    if (clawOpen)
                    {
                        servo.setPosition(0.35);
                    }
                    else
                    {
                        servo.setPosition(0.25);
                    }

                }
                clawPressed = gamepad1.a;
                // arm movement

                if(gamepad1.dpad_up)
                {
                    arm_power = 0.4 * armPowerVariable;
                }
                else if (gamepad1.dpad_down)
                {
                    arm_power = -0.25 * armPowerVariable;
                }
                else
                {
                    arm_power = 0;
                }

                armMotor.setPower(arm_power);
                telemetry.addData("armPosition:", armMotor.getCurrentPosition());
                telemetry.update();

                // movement

                forward = gamepad1.left_stick_y * -speedE;
                strafe = -(gamepad1.right_stick_x * speedE);
                turn = gamepad1.right_stick_x * speedE;
                denominator = JavaUtil.maxOfList(JavaUtil.createListWith(1, Math.abs(forward) + Math.abs(strafe) + Math.abs(turn)));
                telemetry.addData("forwardPower:", forward);
                //movement
                leftFront.setPower((forward - (strafe + turn)) / denominator);
                rightFront.setPower((forward - (strafe - turn)) / denominator);
                leftRear.setPower((forward + (strafe - turn)) / denominator);
                rightRear.setPower((forward + strafe + turn) / denominator);
                // changed +,- to -,- (this fixed the movement controls)

                telemetry.update();

            }
        }

    }

}

// public void encoder(int turnage){
//   ArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//   newTarget = ticks/turnage;
//   ArmMotor.setTargetPosition((int)newTarget);
//   ArmMotor.setPower(0.3);
//   ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
// }

