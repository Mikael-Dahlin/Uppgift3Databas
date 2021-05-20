package main;

import java.util.ArrayList;
import java.util.Scanner;

import bean.MovieBean;
import database.SQLconnection;

public class Start {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		String movieName = "";
		String movieDir = "";
		String actors = "";
		String award = "";
		String actor = "";
		
		ArrayList<MovieBean> movies = new ArrayList<MovieBean>();

		// User interface.
		System.out.println("Add movie? (y/n)");
		String resp = scanner.nextLine();
		if (resp.startsWith("y") || resp.startsWith("Y")) {
			System.out.println("movie name:");
			movieName = scanner.nextLine();
			System.out.println("movie director:");
			movieDir = scanner.nextLine();
			System.out.println("movie actors: (can add multiple with comma separation)");
			actors = scanner.nextLine();
			System.out.println("movie award:");
			award = scanner.nextLine();
			System.out.println("awarded actor: (optional)");
			actor = scanner.nextLine();
		}

		// Check if connection is open.
		if (SQLconnection.connectSQL()) {

			// Insert movies to database.
			SQLconnection.insertMovie(movieName, movieDir);
			SQLconnection.insertActors(actors, movieName);
			SQLconnection.insertAward(award, actor, movieName);
			
			// Send the string
			movies = SQLconnection.actorsInMovie("se7en", new ArrayList<MovieBean>());
			
			// Print data from the bean
			System.out.println("----------------");
			System.out.println("Actors in se7en");
			for (int i = 0; i < movies.size(); i++) {
				System.out.print(movies.get(i).getMovieName());
				System.out.print(" ");
				System.out.println(movies.get(i).getActorName());
			}			
			System.out.println();
			
			// Send the string
			movies = SQLconnection.movieAwardsWithActor("brad pitt", new ArrayList<MovieBean>());

			// Print data from the bean
			System.out.println("Awards to movies with brad pitt in them");
			for (int i = 0; i < movies.size(); i++) {
				System.out.print(movies.get(i).getActorName());
				System.out.print(" ");
				System.out.print(movies.get(i).getMovieName());
				System.out.print(" ");
				System.out.print(movies.get(i).getMovieDirector());
				System.out.print(" ");
				System.out.println(movies.get(i).getAwardName());
			}
			System.out.println();
			
			// Send the string
			movies = SQLconnection.movieAwards("best movie", new ArrayList<MovieBean>());

			// Print data from the bean
			System.out.println("List of movies that got best movie award:");
			for (int i = 0; i < movies.size(); i++) {
				System.out.print(movies.get(i).getAwardName());
				System.out.print(" ");
				System.out.print(movies.get(i).getMovieName());
				System.out.print(" ");
				System.out.println(movies.get(i).getMovieDirector());
			}
			System.out.println();
			
			// Send the string
			movies = SQLconnection.moviesWithActor("brad pitt", new ArrayList<MovieBean>());

			// Print data from the bean
			System.out.println("Movies with brad pitt:");
			for (int i = 0; i < movies.size(); i++) {
				System.out.print(movies.get(i).getActorName());
				System.out.print(" ");
				System.out.println(movies.get(i).getMovieName());
			}
			System.out.println();
			
			SQLconnection.closeConnection();
		}

		scanner.close();
	}

}
