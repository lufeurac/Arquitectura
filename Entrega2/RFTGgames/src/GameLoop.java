import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class GameLoop {

	public int turnos = 0;
	public int VictoryPoints = 52;
	public Player jugadorEnTurno;

	public static ArrayList<Player> jugadores = new ArrayList<Player>();
	public static ArrayList<Integer> fasesJugadas = new ArrayList<Integer>();

	public static Stack<Card> mazo = new Stack<Card>();
	public static ArrayList<Card> pozo = new ArrayList<Card>();

	public static void SetUpJuego() throws IOException {
		CardParser.cargarCartas(mazo);
		Collections.shuffle(mazo);
		/*
		 * for (Card card : mazo) { System.out.println(card.toString()); }
		 */

		// TODO:RECIBIR JUGADORES
		Card temp;
		for (int i = 0; i < 4; i++) {
			Player p = new Player("jugador: " + i);
			temp = mazo.pop();
			p.agregarCartaAltablero(temp);
			pozo.add(temp);
			darCartas(p, 6);
			pagarCartas(p, 2);
			jugadores.add(p);
		}
		System.out.println(mazo.size());

	}

	public static void selecccionarfaces(int key) {
		switch (key) {
		case 1: // case "Explorar_1":
			explorar();
			break;
		case 2: // case "Explorar_2":
			explorar();
			break;
		case 3: // case "Investigar":
			investigar();
			break;
		case 4: // case "Colonizar":
			colonizar();
			break;
		case 5: // case "Comercio":
			comercio();
			break;
		case 6: // case "Consumir":
			consumir();
		case 7: // case "Producir":
			producir();
			break;

		default:
			break;
		}
	}

	public static void producir() {
		for (Player player : jugadores) {
			if (player.getFaseEscogida() == 6) {
				generarRecursos(player, true);
			} else {
				generarRecursos(player, false);
			}
		}
	}

	public static void consumir() {
		// TODO Auto-generated method stub

	}

	public static void comercio() {
		// TODO Auto-generated method stub

	}

	public static void colonizar() {
		for (Player player : jugadores) {
			if (player.getFaseEscogida() == 4) {
				colocarCarta(player, 0, "investigacion");
				darCartas(player, 1);
			} else {
				if (!player.skip()) {
					colocarCarta(player, 0, "investigacion");
				}
			}
		}
	}

	public static void investigar() {
		for (Player player : jugadores) {
			if (player.getFaseEscogida() == 3) {
				colocarCarta(player, -1, "investigacion");
			} else {
				if (!player.skip()) {
					colocarCarta(player, 0, "investigacion");
				}
			}
		}
	}

	public void explorar1() {
		// TODO Auto-generated method stub
		// +3 -1 |+2
		// +2 -1 |+1
	}

	public static void explorar() {
		for (Player player : jugadores) {
			System.out.println("Player " + player.getId());

			if (player.getFaseEscogida() == 1) {
				darCartas(player, 7);
				pagarCartas(player, 6);
			} else {
				if (player.getFaseEscogida() == 2) {
					darCartas(player, 3);
					pagarCartas(player, 1);
				} else {
					darCartas(player, 2);
					pagarCartas(player, 1);
				}
			}

		}
		for (Player player : jugadores) {
			System.out.println(player.getMano().size());
		}
		System.out.println(mazo.size());
		System.out.println(pozo.size());
		// +7 -6 |+1
		// +2 -1 |+1
	}

	public static void pagarCartas(Player p, int cantidad) {
		for (int i = 0; i < cantidad; i++) {
			pozo.add(p.descartarCarta());
		}
	}

	public static void darCartas(Player p, int cantidad) {
		if (cantidad > mazo.size()) {
			llenarMazo();
		}
		Card temp;
		for (int i = 0; i < cantidad; i++) {
			temp = mazo.pop();
			p.agregarCartaAlaMano(temp);
			// pozo.add(temp);
		}
	}

	public static void llenarMazo() {
		mazo.addAll(pozo);
		Collections.shuffle(mazo);
	}

	public static void main(String[] args) throws IOException {
		SetUpJuego();
		/*
		 * while(VictoryPoints != 0 || tableroCompleto()) {
		 */
		seleccionarFase();
		turnoLoop();

		// }
	}

	public static void turnoLoop() {
		if (fasesJugadas.contains(1) && fasesJugadas.contains(2)) {
			fasesJugadas.remove(1);
		}
		for (Integer integer : fasesJugadas) {
			selecccionarfaces(integer);

		}
	}

	public static void seleccionarFase() {
		for (Player player : jugadores) {
			Integer temp = player.seleccionarFase();
			player.setFaseEscogida(temp);
			player.toString();
			if (!fasesJugadas.contains(temp)) {
				fasesJugadas.add(temp);
			}
		}
		Collections.sort(fasesJugadas);
	}

	public boolean tableroCompleto() {
		for (Player player : jugadores) {
			if (player.getTablero().size() == 12) {
				return true;
			}
		}
		return false;
	}

	public static boolean colocarCarta(Player player, int i, String fase) {
		Card carta = player.escogerCarta();
		if (carta.getCosto() > player.getMano().size() - 1) {
			return false;
		} else {
			if (fase.equals("investigacion")) {
				pagarCartas(player, carta.getCosto() + i);
				player.ManoCartaAltablero(carta);
				return true;
			} else {
				if (fase.equals("colonizar")) {
					if (carta.isEsMilitar() && carta.isEsWindfall()) {
						if (carta.getCosto() <= player.getPoderMilitar()) {
							player.ManoCartaAltablero(carta);
							colocarBienCarta(carta);
							return true;
						} else {
							return false;
						}
					}
					if (carta.isEsMilitar()) {
						if (carta.getCosto() <= player.getPoderMilitar()) {
							player.ManoCartaAltablero(carta);
							return true;
						} else {
							return false;
						}
					}
					if (carta.isEsWindfall()) {
						pagarCartas(player, carta.getCosto() + i);
						colocarBienCarta(carta);
						player.ManoCartaAltablero(carta);
						return true;
					}
					pagarCartas(player, carta.getCosto() + i);
					player.ManoCartaAltablero(carta);
				}
				return true;
			}
		}
	}

	private static void colocarBienCarta(Card carta) {
		if (mazo.size()< 5) {
			llenarMazo();
		}
		carta.setBienes(mazo.pop());
	}

	public static void generarRecursos(Player player, boolean b) 
	{
		if(player.producirRecursos())
		{
			
		}
		if(b && player.hasWindafall())
		{
			colocarBienCarta(player.bonusWindfall());
		}
	}
}
