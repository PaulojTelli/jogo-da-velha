package com.example.jogaodavelha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JogoDaVelha()
        }
    }
}

@Composable
fun JogoDaVelha() {
    //variaveis de controle
    var estadoJogo by remember { mutableStateOf(List(9) { "" }) }
    var jogadorAtual by remember { mutableStateOf("X") }
    var jogoAtivo by remember { mutableStateOf(true) }
    var msgEstadoJogo by remember { mutableStateOf("") }

    //lista de posicoes que ganha o jogo
    val condicaoVitoria = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )
//funcao que observa o jogo
    fun verificarGanhador(): String {
        for (posicao in condicaoVitoria) {
            val (a, b, c) = posicao
            if (estadoJogo[a].isNotEmpty() && estadoJogo[a] == estadoJogo[b] && estadoJogo[a] == estadoJogo[c]) {
                jogoAtivo = false
                return estadoJogo[a]
            }
        }
        if (!estadoJogo.contains("")) {
            jogoAtivo = false
            return "EMPATE"
        }
        return ""
    }

    val ganhador = verificarGanhador()

    if (ganhador.isNotEmpty()) {
        msgEstadoJogo = when (ganhador) {
            "EMPATE" -> "O jogo terminou em empate!"
            else -> "O jogador $ganhador venceu!"
        }
    } else {
        msgEstadoJogo = "Vez do jogador $jogadorAtual"
    }
//apenas uma coluna central
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFA9CCE3)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //texto titulo
        Text(
            text = "Jogo da Velha",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        //texto estado do jogo
        Text(
            text = msgEstadoJogo,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (ganhador == "EMPATE") Color.Black else if (ganhador.isNotEmpty()) Color.Green else Color.White, // ↑ Cor do texto: amarelo para empate, verde para vencedor, branco para status normal
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
//tabuleiro
        Grid(gridState = estadoJogo, onCellClicked = { index ->
            if (jogoAtivo && estadoJogo[index].isEmpty()) {
                estadoJogo = estadoJogo.toMutableList().apply {
                    this[index] = jogadorAtual
                }
                jogadorAtual = if (jogadorAtual == "X") "O" else "X"
                verificarGanhador() // Atualiza o status do jogo após cada jogada
            }
        })

        Spacer(modifier = Modifier.height(16.dp))
//reset
        Button(
            onClick = {
                estadoJogo = List(9) { "" }
                jogadorAtual = "X"
                jogoAtivo = true
                msgEstadoJogo = "Vez do jogador $jogadorAtual"
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF777777),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Reiniciar Jogo")
        }
    }
}

//funcao tabuleiro do jogo
@Composable
fun Grid(gridState: List<String>, onCellClicked: (Int) -> Unit) {
    Column {
        for (i in 0 until 3) {
            Row {
                for (j in 0 until 3) {
                    val index = i * 3 + j
                    Cell(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(80.dp)
                            .clickable { onCellClicked(index) },
                        text = gridState[index]
                    )
                }
            }
        }
    }
}

//funcao de cada quadrado
@Composable
fun Cell(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier
            .background(Color.LightGray, RoundedCornerShape(10.dp))
            .border(BorderStroke(2.dp, Color.DarkGray), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 24.sp, color = Color.Black)
    }
}

@Preview
@Composable
fun PreviewJogoDaVelha() {
    JogoDaVelha()
}
