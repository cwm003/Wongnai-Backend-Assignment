package com.wongnai.interview.movie.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.MovieSearchService;

@Component("invertedIndexMovieSearchService")
@DependsOn("movieDatabaseInitializer")
public class InvertedIndexMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieRepository movieRepository;

	private Map<String, List<Long>> invertedIndexTable;

	public InvertedIndexMovieSearchService(){
		invertedIndexTable = new TreeMap<>();
		for(Movie movie:movieRepository.findAll()){
			for(String word : movie.getName().split(" ")){
				String wordLowerCase = word.toLowerCase();
				Long id = movie.getId();
				if(invertedIndexTable.containsKey(wordLowerCase)){
					invertedIndexTable.get(wordLowerCase).add(id);
				}else{
					List<Long> id_list = new ArrayList<>();
					id_list.add(id);
					invertedIndexTable.put(wordLowerCase,id_list);
				}
			}
		}
	}

	@Override
	public List<Movie> search(String queryText) {

		return null;
	}
}
