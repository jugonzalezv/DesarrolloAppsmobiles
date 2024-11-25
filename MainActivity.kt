package com.example.triqui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triqui.ui.theme.TriquiTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriquiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TriquiGame(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TriquiGame(modifier: Modifier = Modifier) {
    var board by remember { mutableStateOf(
        List(3) { MutableList(3) { "" } } // El tablero vacío
    ) }
    var isXTurn by remember { mutableStateOf(true) }
    var winner by remember { mutableStateOf<String?>(null) }
    var isDraw by remember { mutableStateOf(false) } // Variable para manejar empate

    fun resetGame() {
        board = List(3) { MutableList(3) { "" } }
        isXTurn = true
        winner = null
        isDraw = false // Reiniciar estado de empate
    }

    fun checkWinner(): String? {
        // Verificar filas, columnas y diagonales
        for (i in 0..2) {
            if (board[i][0] != "" && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                return board[i][0]
            }
            if (board[0][i] != "" && board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
                return board[0][i]
            }
        }
        if (board[0][0] != "" && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            return board[0][0]
        }
        if (board[0][2] != "" && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            return board[0][2]
        }
        return null
    }

    // Función para verificar empate
    fun checkDraw(): Boolean {
        for (row in board) {
            for (cell in row) {
                if (cell == "") {
                    return false // Si alguna celda está vacía, no es empate
                }
            }
        }
        return winner == null // Si no hay ganador y el tablero está lleno, es empate
    }

    // Verificar ganador después de cada jugada
    winner = checkWinner()

    // Verificar empate si no hay ganador
    if (winner == null) {
        isDraw = checkDraw()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Mostrar el estado del juego
        when {
            winner != null -> {
                Text("Jugador $winner gana!", style = MaterialTheme.typography.bodyLarge)
                Button(onClick = { resetGame() }) {
                    Text("Nuevo Juego")
                }
            }
            isDraw -> {
                Text("¡Empate!", style = MaterialTheme.typography.bodyLarge)
                Button(onClick = { resetGame() }) {
                    Text("Nuevo Juego")
                }
            }
            else -> {
                Text("Turno de: ${if (isXTurn) "X" else "O"}", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el tablero
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            for (row in 0 until 3) {
                Row(horizontalArrangement = Arrangement.Center) {
                    for (col in 0 until 3) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(4.dp)
                                .background(Color.Gray)
                                .clickable {
                                    if (board[row][col].isEmpty() && winner == null && !isDraw) {
                                        board[row][col] = if (isXTurn) "X" else "O"
                                        isXTurn = !isXTurn
                                        winner = checkWinner() // Verificar si hay ganador después de la jugada
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            BasicText(
                                text = board[row][col],
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TriquiGamePreview() {
    TriquiTheme {
        TriquiGame()
    }
}
