package com.kramirez.domotica;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Switch seguridadSwitch, interiorSwitch, salaSwitch, cocinaSwitch, dormitorioSwitch, bañoSwitch, patioSwitch, carportSwitch,  exteriorSwitch;
    private Button abrirPortonButton, cerrarPortonButton;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothGatt bluetoothGatt;
    BluetoothGattCharacteristic bluetoothGattCharacteristic;
    BluetoothDevice bluetoothDevice;
    final Handler handler = new Handler();

    private static String BLE_MAC_ADDRESS = "B4:99:4C:56:97:4E";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        seguridadSwitch = findViewById(R.id.seguridadSwitch);

        abrirPortonButton = findViewById(R.id.abrirPortonButton);
        cerrarPortonButton = findViewById(R.id.cerrarPortonButton);

        interiorSwitch = findViewById(R.id.interiorSwitch);
        salaSwitch = findViewById(R.id.salaSwitch);
        cocinaSwitch = findViewById(R.id.cocinaSwitch);
        dormitorioSwitch = findViewById(R.id.dormitorioSwitch);
        bañoSwitch = findViewById(R.id.bañoSwitch);

        patioSwitch = findViewById(R.id.patioSwitch);
        carportSwitch = findViewById(R.id.carportSwitch);
        exteriorSwitch = findViewById(R.id.exteriorSwitch);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth no disponible", Toast.LENGTH_LONG);
        } else {
            bluetoothDevice = mBluetoothAdapter.getRemoteDevice(BLE_MAC_ADDRESS);
            bluetoothDevice.connectGatt(this, true, bluetoothGattCallback);
        }

        interiorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                salaSwitch.setChecked(b);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cocinaSwitch.setChecked(b);
                    }
                }, 50);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dormitorioSwitch.setChecked(b);
                    }
                }, 100);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bañoSwitch.setChecked(b);
                    }
                }, 150);

            }
        });

        seguridadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateInteriorCheck();
                if (b) {
                    enviarComando("A");
                } else {
                    enviarComando("B");
                }
            }
        });


        abrirPortonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarComando("C");
            }
        });

        cerrarPortonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarComando("D");
            }
        });

        salaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateInteriorCheck();
                if (b) {
                    enviarComando("E");
                } else {
                    enviarComando("F");
                }

            }
        });

        cocinaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateInteriorCheck();
                if (b) {
                    enviarComando("G");
                } else {
                    enviarComando("H");
                }
            }
        });

        dormitorioSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateInteriorCheck();
                if (b) {
                    enviarComando("I");
                } else {
                    enviarComando("J");
                }
            }
        });

        bañoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateInteriorCheck();
                if (b) {
                    enviarComando("K");
                } else {
                    enviarComando("L");
                }
            }
        });

        exteriorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean bb) {
                patioSwitch.setChecked(bb);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        carportSwitch.setChecked(bb);
                    }
                }, 50);
            }
        });

        patioSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateExteriorCheck();
                if (b) {
                    enviarComando("M");
                } else {
                    enviarComando("N");
                }
            }
        });

        carportSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateExteriorCheck();
                if (b) {
                    enviarComando("O");
                } else {
                    enviarComando("P");
                }
            }
        });

        updateInteriorCheck();
        updateExteriorCheck();


        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);

    }

    BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                System.out.println("STATE_CONNECTED");
                if (bluetoothGatt == null) {
                    gatt.discoverServices();
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            System.out.println("onServicesDiscovered  ++++++++++++   BUSCANDO SERVICIOS  ++++++++++++");

            for (BluetoothGattService gattService : gatt.getServices()) {
                for (BluetoothGattCharacteristic characteristic : gattService.getCharacteristics()) {
                    if (characteristic.getUuid().toString().equals("0000ffe1-0000-1000-8000-00805f9b34fb")) {
                        bluetoothGattCharacteristic = characteristic;
                        bluetoothGatt = gatt;
                        bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
                    }
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            final byte[] data = characteristic.getValue();
            String message = new String(data);
            System.out.println("onCharacteristicChanged: " + message);
            if (message.equals("Z")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        triggerAlarm();
                    }
                });

            }
        }
    };

    private void enviarComando(String comando) {
        if (bluetoothDevice != null && bluetoothGattCharacteristic != null && bluetoothGatt != null) {
            bluetoothGattCharacteristic.setValue(comando);
            bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
        }
    }

    private void updateInteriorCheck() {
        if ((salaSwitch.isChecked() == cocinaSwitch.isChecked())
                && (salaSwitch.isChecked() == dormitorioSwitch.isChecked())
                && (salaSwitch.isChecked() == bañoSwitch.isChecked())
                && (cocinaSwitch.isChecked() == dormitorioSwitch.isChecked())
                && (cocinaSwitch.isChecked() == bañoSwitch.isChecked())
                && (dormitorioSwitch.isChecked() == bañoSwitch.isChecked())) {

            if (salaSwitch.isChecked() && cocinaSwitch.isChecked() && dormitorioSwitch.isChecked() && bañoSwitch.isChecked()) {
                interiorSwitch.setChecked(true);
            } else {
                interiorSwitch.setChecked(false);
            }
        }
    }

    private void updateExteriorCheck() {
        if (carportSwitch.isChecked() == patioSwitch.isChecked()) {
            exteriorSwitch.setChecked(carportSwitch.isChecked());
        }
    }
    private boolean isShowing = false;

    private void triggerAlarm() {
        if (!isShowing) {
            final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = { 0, 400, 200 };
            v.vibrate(pattern, 0);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("¡Alarma!");
            builder.setMessage("Alguien ha abierto la puerta principal.");

            builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    v.cancel();
                    enviarComando("Q");
                    isShowing = false;
                }
            });

            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    v.cancel();
                    enviarComando("Q");
                    isShowing = false;
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
            isShowing = true;
        }
    }
}
