package com.trs.tickets.service;

import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private static final int NEIGHBORHOOD_SIZE = 10;
    private static final int RECOMMENDATIONS_AMOUNT = 10;

    private final DataSource dataSource;

    public List<RecommendedItem> recommendMoviesForUser(Long userID) {
        try {

            DataModel datamodel = new MySQLJDBCDataModel(dataSource, "rating", "user_id", "movie_id", "score", null);

            UserSimilarity usersimilarity = new PearsonCorrelationSimilarity(datamodel);

            UserNeighborhood userneighborhood = new NearestNUserNeighborhood(NEIGHBORHOOD_SIZE, usersimilarity, datamodel);

            UserBasedRecommender recommender = new GenericUserBasedRecommender(datamodel, userneighborhood, usersimilarity);

            System.out.println("Most similar users IDs: " + Arrays.toString(recommender.mostSimilarUserIDs(userID, RECOMMENDATIONS_AMOUNT)));
            List<RecommendedItem> recommendations = recommender.recommend(userID, RECOMMENDATIONS_AMOUNT);

            for (RecommendedItem recommendation : recommendations) {
                System.out.println(recommendation);
            }
            return recommendations;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }
}

/*
DataModel from File example
public List<RecommendedItem> recommendMoviesForUserFromFile(Long userId) {
        try {
            DataModel datamodel = new FileDataModel(new File("FILENAME.csv"));
            //Creating UserSimilarity object.
            UserSimilarity usersimilarity = new PearsonCorrelationSimilarity(datamodel);

            //Creating UserNeighbourHHood object.
            UserNeighborhood userneighborhood = new NearestNUserNeighborhood(NEIGHBORHOOD_SIZE, usersimilarity, datamodel);

            //Create UserRecomender
            UserBasedRecommender recommender = new GenericUserBasedRecommender(datamodel, userneighborhood, usersimilarity);

            System.out.println("Most similar users IDs: " + Arrays.toString(recommender.mostSimilarUserIDs(userId, 10)));
            List<RecommendedItem> recommendations = recommender.recommend(userId, 10);

            for (RecommendedItem recommendation : recommendations) {
                System.out.println(recommendation);
            }
            return recommendations;

        } catch (Exception e) {
        }
        return null;
    }

 */

