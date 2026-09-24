package com.example.calculadoraimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraimc.ui.theme.CalculadoraImcTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraImcTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IMCscreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun IMCscreen(modifier: Modifier = Modifier) {
    // Estados do Formulário
    var pesoState by remember { mutableStateOf("") }
    var alturaState by remember { mutableStateOf("") }
    var imcState by remember { mutableDoubleStateOf(0.0) }
    var statusImcState by remember { mutableStateOf("") }

    // Cor dinâmica baseada no IMC
    var corResultado = when {
        imcState == 0.0 -> colorResource(R.color.cor_app)
        imcState < 18.5 -> Color(0xFFE53935) // Abaixo do peso (Vermelho)
        imcState < 25.0 -> Color(0xFF4CAF50) // Ideal (Verde)
        imcState < 30.0 -> Color(0xFFFF9800) // Levemente Acima (Laranja)
        imcState < 35.0 -> Color(0xFFD32F2F) //Obesidade Grau I
        imcState< 40.0 -> Color(0xFFD32F2F) //Obesidade Grau II
        else -> Color(0xFFD32F2F) //Obesidade Grau III
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // -- Header --
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(color = colorResource(R.color.cor_app)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 16.dp, bottom = 8.dp),
                        painter = painterResource(R.drawable.balanca),
                        contentDescription = "Logo App"
                    )

                    Text(
                        text = "Calculadora de IMC",
                        fontSize = 22.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // -- Formulário / Card --
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-30).dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF9F6F6)
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Seus Dados",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.cor_app)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Campo Peso
                            OutlinedTextField(
                                value = pesoState,
                                onValueChange = { pesoState = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Seu peso (Kg)") },
                                placeholder = { Text("Ex: 75.5") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Campo Altura
                            OutlinedTextField(
                                value = alturaState,
                                onValueChange = { alturaState = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Sua altura (m ou cm)") },
                                placeholder = { Text("Ex: 1.75 ou 175") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Botão de Calcular
                            Button(
                                onClick = {
                                    val peso = pesoState.replace(",", ".").toDoubleOrNull()
                                    var altura = alturaState.replace(",", ".").toDoubleOrNull()

                                    if (peso != null && altura != null && altura > 0) {
                                        if (altura > 3.0) {
                                            altura /= 100
                                        }

                                        val imc = peso / (altura * altura)
                                        imcState = imc

                                        statusImcState = when {
                                            imc < 18.5 -> "Abaixo do Peso"
                                            imc < 25.0 -> "Peso Ideal"
                                            imc < 30.0 -> "Levemente Acima do Peso"
                                            imc < 35.0 -> "Obesidade Grau I"
                                            imc < 40.0 -> "Obesidade Grau II"
                                            else -> "Obesidade Grau III"
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.cor_app),

                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "CALCULAR",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            //Botão para limpar
                            Button(
                                onClick = {
                                    pesoState = ""
                                    alturaState = ""
                                    imcState = 0.0
                                    statusImcState = ""


                                },
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(35.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red,

                                    ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "LIMPAR",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // -- Exibição do Resultado --
                    if (imcState > 0) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = (-15).dp),
                            colors = CardDefaults.cardColors(
                                containerColor = corResultado
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Resultado",
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = String.format(Locale.getDefault(), "%.1f", imcState),
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Classificação",
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = statusImcState,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
