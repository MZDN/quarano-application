import { Component, OnInit } from '@angular/core';
import {roleNames} from '../../../models/role';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'qro-header-left',
  templateUrl: './header-left.component.html',
  styleUrls: ['./header-left.component.scss']
})
export class HeaderLeftComponent implements OnInit {
  public readonly isLoggedIn$ = this.userService.isLoggedIn$;
  public rolesNames = roleNames;

  constructor(
    private userService: UserService) { }

  ngOnInit() {
  }

}
