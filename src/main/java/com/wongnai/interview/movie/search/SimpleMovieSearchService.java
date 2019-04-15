package com.wongnai.interview.movie.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieDataService;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieDataService movieDataService;

	@Override
	public List<Movie> search(String queryText) {
		MoviesResponse movies_list = movieDataService.fetchAll();
		List<Movie> search_result = new ArrayList<>();
		if(!queryText.contains(" ")) {
			Pattern p = Pattern.compile("\\b" + queryText.toLowerCase() + "\\b");
			for (MovieData movie : movies_list) {
				if (p.matcher(movie.getTitle().toLowerCase()).find()) {
					Movie tmp = new Movie(movie.getTitle());
					tmp.getActors().addAll(movie.getCast());
					search_result.add(tmp);
				}
			}
		}
		return search_result;
	}
}
