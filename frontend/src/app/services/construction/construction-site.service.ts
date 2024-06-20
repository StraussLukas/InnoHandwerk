import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ConstructionSite} from "../../model/constructionSite";

@Injectable({
  providedIn: 'root'
})
export class ConstructionSiteService {
  private apiUrl = 'http://localhost:8080/';

  constructor(private http: HttpClient) { }

  getAllConstructionSites(): Observable<ConstructionSite[]> {
    return this.http.get<ConstructionSite[]>(this.apiUrl + 'baustelle');
  }

  getConstructionSitesByPersonalnummer(personalnummer: string): Observable<ConstructionSite[]> {
    return this.http.get<ConstructionSite[]>(this.apiUrl + 'baustellenbypersonalnummer/' + personalnummer);
  }

  getConstructionSitesByStatus(status: string): Observable<ConstructionSite[]> {
    return this.http.get<ConstructionSite[]>(this.apiUrl + 'baustellenbystatus/' + status);
  }
}
