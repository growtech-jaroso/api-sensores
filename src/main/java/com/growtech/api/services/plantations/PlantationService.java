package com.growtech.api.services.plantations;

import com.growtech.api.dtos.requests.EditPlantationDto;
import com.growtech.api.dtos.requests.PlantationManagerDto;
import com.growtech.api.dtos.requests.PlantationDto;
import com.growtech.api.dtos.responses.OwnerInfo;
import com.growtech.api.entities.Plantation;
import com.growtech.api.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Service
public interface PlantationService {
  Mono<Tuple2<List<Plantation>, Long>> getAllPlantationsByUserPaginated(
    User user,
    String plantationSearchFilter,
    String plantationStatus,
    PageRequest pageRequest
  );
  Mono<Tuple2<List<OwnerInfo>, Long>> getAllPlantationsOwners(PageRequest pageRequest);
  Mono<Plantation> createPlantation(PlantationDto plantationDto);
  Mono<String> addPlantationManager(User user, String plantationId, PlantationManagerDto plantationManagerDto);
  Mono<String> deletePlantationManager(User user, String plantationId, PlantationManagerDto plantationManagerDto);
  Mono<String> deletePlantation(String plantationId);
  Mono<Plantation> editPlantation(User user, String plantationId, EditPlantationDto plantationDto);
  Mono<List<Plantation>> getPlantationsByUserId(String userId);
}
