import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RewardService } from '../../services/reward/reward.service';
import { Reward } from '../../services/interfaces/reward';

@Component({
  selector: 'app-rewards',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './rewards.component.html',
  styleUrls: ['./rewards.component.scss']
})
export class RewardsComponent {
  rewards: Reward[] = [];

  constructor(private rewardService: RewardService) {
    this.rewardService.getRecompensas().subscribe(data => {
      this.rewards = data;
    });
  }
}
