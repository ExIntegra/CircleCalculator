package com.circlecalculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText radiusInput;
    private TextView diameterResult;
    private TextView lengthResult;
    private TextView areaResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        radiusInput = findViewById(R.id.radiusInput);
        diameterResult = findViewById(R.id.diameterResult);
        lengthResult = findViewById(R.id.lengthResult);
        areaResult = findViewById(R.id.areaResult);
        Button calculateButton = findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(view -> calculate());
    }

    private void calculate() {
        String input = radiusInput.getText().toString().trim();
        if (input.isEmpty()) {
            showError(R.string.error_empty);
            return;
        }

        double radius;
        try {
            // Принимаем десятичную запятую и точку.
            radius = Double.parseDouble(input.replace(',', '.'));
        } catch (NumberFormatException exception) {
            showError(R.string.error_format);
            return;
        }

        if (radius <= 0) {
            showError(R.string.error_range);
            return;
        }
        if (!Double.isFinite(radius)) {
            showError(R.string.error_large);
            return;
        }

        // Основные вычисления собраны в отдельном методе.
        double[] results = calculateCircle(radius);
        if (!Double.isFinite(results[0]) || !Double.isFinite(results[1])
                || !Double.isFinite(results[2])) {
            showError(R.string.error_large);
            return;
        }

        Locale locale = Locale.forLanguageTag("ru-RU");
        diameterResult.setText(getString(R.string.diameter_result,
                String.format(locale, "%.2f", results[0])));
        lengthResult.setText(getString(R.string.length_result,
                String.format(locale, "%.2f", results[1])));
        areaResult.setText(getString(R.string.area_result,
                String.format(locale, "%.2f", results[2])));
    }

    // Возвращает диаметр, длину и площадь в этом порядке.
    private double[] calculateCircle(double radius) {
        return new double[]{2 * radius, 2 * Math.PI * radius, Math.PI * radius * radius};
    }

    private void showError(int messageId) {
        diameterResult.setText(R.string.diameter_placeholder);
        lengthResult.setText(R.string.length_placeholder);
        areaResult.setText(R.string.area_placeholder);
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
    }
}
