package com.wongnai.interview.movie.search;

import java.util.*;

import org.assertj.core.util.Lists;
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

	private Map<String, List<Long>> invertedIndexTable = null;

	private void createInvertedIndexTable(){
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
		if(invertedIndexTable == null){
			createInvertedIndexTable();
		}Set<Long> id = null;
		for(String word : queryText.split(" ")){
			String wordLowerCase = word.toLowerCase();
			if(invertedIndexTable.containsKey(wordLowerCase)){
				if(id == null){
					id = new HashSet<>(invertedIndexTable.get(wordLowerCase));
				}else{
					id.retainAll(invertedIndexTable.get(wordLowerCase));
				}
			}
		}
		List<Movie> movies_list = new ArrayList<>();
		try{
			movies_list = Lists.newArrayList(movieRepository.findAllById(id));
		}catch (Exception e){
			e.printStackTrace();
		}
		return movies_list;
	}
}
